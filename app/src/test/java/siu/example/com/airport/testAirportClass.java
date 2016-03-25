package siu.example.com.airport;

import org.junit.Test;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class testAirportClass{

    private static Airport mTestAirport = new Airport("San Francisco International Airport",
            37.618763,
            -122.3823531,
            "San Francisco International Airport PO Box 8097",
            "San Francisco",
            "California",
            94128,
            "San Francisco International Airport is an international" +
                    " airport 13 miles south of downtown San Francisco," +
                    " California, United States, near Millbrae and " +
                    "San Bruno in unincorporated San Mateo County.",
            "false");

    @Test
    public void testAirportName() {
        String actual = mTestAirport.getName();
        String expected = "San Francisco International Airport";

        org.junit.Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAirportAddress(){
        String actual = mTestAirport.getAddress();
        String expected = "San Francisco International Airport PO Box 8097";

        org.junit.Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAirportCity(){
        String actual = mTestAirport.getCity();
        String expected = "San Francisco";

        org.junit.Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAirportState(){
        String actual = mTestAirport.getState();
        String expected = "California";

        org.junit.Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAirportZip(){
        int actual = mTestAirport.getZip();
        int expected = 94128;

        org.junit.Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAirportLatitude(){
        Double actual = mTestAirport.getLatitude();
        Double expected = 37.618763;

        org.junit.Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAirportLongitude(){
        Double actual = mTestAirport.getLongitude();
        Double expected = -122.3823531;

        org.junit.Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAirportDescription(){
        String actual = mTestAirport.getDescription();
        String expected = "San Francisco International Airport is an international" +
                " airport 13 miles south of downtown San Francisco," +
                " California, United States, near Millbrae and " +
                "San Bruno in unincorporated San Mateo County.";

        org.junit.Assert.assertEquals(expected, actual);
    }


}