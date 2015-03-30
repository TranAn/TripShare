package com.itpro.tripshare.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.born2go.server.DataServiceImpl;
import com.born2go.shared.Trip;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;

import static com.itpro.tripshare.test.ObjService.factory;

public class TripServiceTest {
	public final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalTaskQueueTestConfig(),
			new LocalDatastoreServiceTestConfig());

	@Before
	public void setUp() {
		factory().register(Trip.class);
		helper.setUp();
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void testInsertTask() {
		DataServiceImpl service = new DataServiceImpl();
	}

	private Trip createTrip() {
		Trip trip = new Trip();
		trip.setName("Hà Nội đi Sapa");
		trip.setDescription("Chuyến đi sẽ đi qua Yên Bái, Dừng chân ở Lào Cai ăn sáng và cuối cùng là đến Sapa");
		trip.setOwner(123L);
		return trip;
	}
}
