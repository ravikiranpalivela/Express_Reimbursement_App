package com.tekskills.er_tekskills.presentation.di

import com.tekskills.er_tekskills.presentation.adapter.ActionItemsOpportunitysAdapter
import com.tekskills.er_tekskills.presentation.adapter.CategoryAdapter
import com.tekskills.er_tekskills.presentation.adapter.ClientEscalationOpportunitysAdapter
import com.tekskills.er_tekskills.presentation.adapter.ClientWiseEscalationAdapter
import com.tekskills.er_tekskills.presentation.adapter.CommentsOpportunitysAdapter
import com.tekskills.er_tekskills.presentation.adapter.MomActionItemsOpportunitysAdapter
import com.tekskills.er_tekskills.presentation.adapter.ProjectWiseActionItemsAdapter
import com.tekskills.er_tekskills.presentation.adapter.TasksAdapter
import com.tekskills.er_tekskills.presentation.adapter.ViewMeetingPurposeAdapter
import com.tekskills.er_tekskills.presentation.adapter.ViewOpportunitysAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object SingletonAdapterModule {
    @Provides
    @Singleton
    @Named("base_fragment")
    fun provideTaskAdapterToBaseFragment() = TasksAdapter()

    @Provides
    @Singleton
    @Named("completed_task_fragment")
    fun provideTaskAdapterToCompletedTasksFragment() = TasksAdapter()

    @Provides
    @Singleton
    @Named("view_opportunity_fragment")
    fun provideTaskAdapterToViewOpportunityFragment() = ViewOpportunitysAdapter()

    @Provides
    @Singleton
    @Named("view_meetings")
    fun provideTaskAdapterToViewMeetingPurposeAdapter() = ViewMeetingPurposeAdapter()

    @Provides
    @Singleton
    @Named("view_main_meetings")
    fun provideTaskAdapterToViewMainMeetingPurposeAdapter() = ViewMeetingPurposeAdapter()

    @Provides
    @Singleton
    @Named("client_wise_escalation_fragment")
    fun provideTaskAdapterToClientWiseEscalationListFragment() = ClientWiseEscalationAdapter()

    @Provides
    @Singleton
    @Named("project_wise_pending_action_item_fragment")
    fun provideTaskAdapterToProjectWiseActionItemsListFragment() = ProjectWiseActionItemsAdapter()

    @Provides
    @Singleton
    @Named("opportunity_details_fragment")
    fun provideEscalationListToOpportunityDetailsFragment() = ClientEscalationOpportunitysAdapter()

    @Provides
    @Singleton
    @Named("action_item_opportunity_details_fragment")
    fun provideActionItemsOpportunityListToOpportunityDetailsFragment() = ActionItemsOpportunitysAdapter()

    @Provides
    @Singleton
    @Named("mom_action_item_opportunity_details_fragment")
    fun provideMomActionItemsOpportunityListToOpportunityDetailsFragment() = MomActionItemsOpportunitysAdapter()

    @Provides
    @Singleton
    @Named("comments_opportunity_details_fragment")
    fun provideCommentItemsOpportunityListToOpportunityDetailsFragment() = CommentsOpportunitysAdapter()


    @Provides
    @Singleton
    fun provideCategoryAdapter() = CategoryAdapter()

}