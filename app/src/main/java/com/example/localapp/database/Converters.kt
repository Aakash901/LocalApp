package com.example.localapp.database

import com.example.localapp.model.ResultData

object Converters {

}

fun ResultData.toResultEntity(): ResultEntity {
    return ResultEntity(
        id = id,
        advertiser = advertiser,
        amount = amount ?: "",
        button_text = button_text ?: "",
        city_location = city_location,
        company_name = company_name ?: "",
        content = content ?: "",
        created_on = created_on ?: "",
        custom_link = custom_link ?: "",
        enable_lead_collection = enable_lead_collection,
        experience = experience,
        expire_on = expire_on ?: "",
        fb_shares = fb_shares,
        fees_charged = fees_charged,
        fees_text = fees_text ?: "",
        is_applied = is_applied,
        is_bookmarked = is_bookmarked,
        is_job_seeker_profile_mandatory = is_job_seeker_profile_mandatory,
        is_owner = is_owner,
        is_premium = is_premium,
        job_category = job_category ?: "",
        job_category_id = job_category_id,
        job_hours = job_hours ?: "",
        job_location_slug = job_location_slug ?: "",
        job_role = job_role ?: "",
        job_role_id = job_role_id,
        job_type = job_type,
        locality = locality,
        num_applications = num_applications,
        openings_count = openings_count,
        other_details = other_details ?: "",
        premium_till = premium_till ?: "",
        qualification = qualification,
        salary_max = salary_max,
        salary_min = salary_min,
        shares = shares,
        shift_timing = shift_timing,
        should_show_last_contacted = should_show_last_contacted,
        status = status,
        title = title ?: "",
        type = type,
        updated_on = updated_on ?: "",
        views = views,
        whatsapp_no = whatsapp_no ?: ""
    )
}
