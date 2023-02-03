package org.hyperskill.app.topics_to_discover_next.injection

import org.hyperskill.app.topics_to_discover_next.presentation.TopicsToDiscoverNextActionDispatcher
import org.hyperskill.app.topics_to_discover_next.presentation.TopicsToDiscoverNextReducer

interface TopicsToDiscoverNextComponent {
    val topicsToDiscoverNextReducer: TopicsToDiscoverNextReducer
    val topicsToDiscoverNextActionDispatcher: TopicsToDiscoverNextActionDispatcher
}