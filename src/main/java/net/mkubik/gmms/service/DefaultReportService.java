package net.mkubik.gmms.service;

import lombok.RequiredArgsConstructor;
import net.mkubik.gmms.api.model.RevenueReportEntry;
import net.mkubik.gmms.api.model.RevenueReportResponse;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.*;

@Service
@RequiredArgsConstructor
public class DefaultReportService implements ReportService {

    private final DSLContext dsl;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public RevenueReportResponse getRevenueReport() {
        var gym = table("gym").as("g");
        var membershipPlan = table("membership_plan").as("mp");
        var member = table("member").as("m");

        var gymName = field("g.name", String.class);
        var planPrice = field("mp.price", BigDecimal.class);
        var planCurrency = field("mp.currency", String.class);
        var memberStatus = field("m.status", String.class);

        List<RevenueReportEntry> entries = dsl.select(
                        gymName.as("gym_name"),
                        sum(planPrice).as("total_revenue"),
                        planCurrency.as("currency")
                )
                .from(member)
                .join(membershipPlan).on(field("m.membership_plan_id").eq(field("mp.id")))
                .join(gym).on(field("mp.gym_id").eq(field("g.id")))
                .where(memberStatus.eq("ACTIVE"))
                .groupBy(gymName, planCurrency)
                .orderBy(gymName.asc(), field("total_revenue").desc())
                .fetch(record -> new RevenueReportEntry(
                        Optional.ofNullable(record.get("gym_name", String.class)),
                        Optional.ofNullable(record.get("total_revenue", BigDecimal.class)),
                        Optional.ofNullable(record.get("currency", String.class))
                ));

        return new RevenueReportResponse(entries);
    }
}