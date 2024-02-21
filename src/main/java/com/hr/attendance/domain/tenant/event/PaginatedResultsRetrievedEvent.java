package com.hr.attendance.domain.tenant.event;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationEvent;
import org.springframework.data.domain.Page;

/**
 *This event will be published when the API retrieves Paginated information and may allow other beans to process
 * the event for communicating the page info with the caller.
 * @author Adil Khalil
 */

@SuppressWarnings({"serial", "rawtypes"})
public final class PaginatedResultsRetrievedEvent extends ApplicationEvent {
	private final HttpServletResponse response;
	private final Page page;
    
	public PaginatedResultsRetrievedEvent( HttpServletResponse response, Page page) {
		super(page);
		this.response = response;
		this.page = page;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public Page getPage() {
		return page;
	}
}
