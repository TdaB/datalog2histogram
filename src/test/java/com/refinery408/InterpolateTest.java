package com.refinery408;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class InterpolateTest {
    @Test
    public void testInterpolate1() throws Exception {
        String name = "test_interpolation_1.csv";
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(name).getFile());
        CSVParser parser = new CSVParser(file);
        TableConfig config = new TableConfig("xlabel", 0.0, 1.0, 1.0,
                                             "ylabel", 0.0, 2.0, 2.0,
                                             "zlabel", 0.0, null, null,
                                             0, 3);
        parser.parseCsv(config);
        Point p = new Point(1, 2);
        assertEquals(3, parser.getData().get(p).getAverage());
    }

    @Test
    public void testInterpolate2() throws Exception {
        String name = "test_interpolation_2.csv";
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(name).getFile());
        CSVParser parser = new CSVParser(file);
        TableConfig config = new TableConfig("xlabel", 0.0, 10.0, 10.0,
                                             "ylabel", 0.0, 10.0, 10.0,
                                             "zlabel", 0.0, null, null,
                                             0, 3);
        parser.parseCsv(config);
        Point p = new Point(0, 0);
        assertEquals(15, parser.getData().get(p).getAverage());
    }

    @Test
    public void testInterpolate3() throws Exception {
        String name = "test_interpolation_3.csv";
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(name).getFile());
        CSVParser parser = new CSVParser(file);
        TableConfig config = new TableConfig("xlabel", 0.0, 10.0, 10.0,
                                             "ylabel", 0.0, 10.0, 10.0,
                                             "zlabel", 0.0, null, null,
                                             0, 3);
        parser.parseCsv(config);
        Point p = new Point(0, 0);
        assertNull(parser.getData().get(p));
    }
}
