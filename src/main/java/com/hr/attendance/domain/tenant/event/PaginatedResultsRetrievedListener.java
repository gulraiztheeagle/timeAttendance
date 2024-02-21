package com.hr.attendance.domain.tenant.event;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;

/**
 * This listener listens to the PaginatedResultsRetrievedEvent and writes to the HttpServeletReponse the paging
 * information like hasNex, hasPrevious, pageSize, totalPages etc.
 * @author Adil Khalil
 */

@SuppressWarnings("rawtypes")
@Component
class PaginatedResultsRetrievedListener implements ApplicationListener<PaginatedResultsRetrievedEvent> {
	private final String PAGE_SIZE = "page-size";
	private final String TOTAL_PAGES = "total-pages";
	private final String CURRENT_PAGE_INDEX = "current-page-index";
	private final String PAGE_ITEMS = "page-items";
	private final String TOTAL_ITEMS = "total-page-items";
	private final String HAS_NEXT_PAGE = "has-next-page";
	private final String HAS_PREVIOUS_PAGE = "has-prev-page";

	@Override
	public void onApplicationEvent(final PaginatedResultsRetrievedEvent event) {
		Preconditions.checkNotNull(event);
		Preconditions.checkNotNull(event.getPage());
		
		Page page = event.getPage();
		HttpServletResponse response = event.getResponse();
		response.addIntHeader(PAGE_SIZE, page.getSize());
		response.addIntHeader(TOTAL_PAGES, page.getTotalPages());
		response.addIntHeader(CURRENT_PAGE_INDEX, page.getNumber());
		response.addIntHeader(PAGE_ITEMS, page.getNumberOfElements());
		response.addHeader(TOTAL_ITEMS, String.valueOf(page.getTotalElements()));
		response.addHeader(HAS_NEXT_PAGE, String.valueOf(page.hasNext()));
		response.addHeader(HAS_PREVIOUS_PAGE, String.valueOf(page.hasPrevious()));
	}
}