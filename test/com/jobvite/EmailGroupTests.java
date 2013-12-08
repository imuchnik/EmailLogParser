package com.jobvite;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import java.io.IOException;


public class EmailGroupTests {
    LogParser logParser;
    String group1;
    String group2;
    String group3;

    @Before
    public void setup() throws NoSuchMethodException {
        logParser = new LogParser();
        group1 = "blah";
        group2 = "bleh";
        group3 = "blue";
        logParser.addGroup(group1);
        logParser.addGroup(group2);
        logParser.addGroup(group3);
    }

    @Test
    public void shouldCreateEmailCollection() {
        Assert.assertNotNull(logParser.emails);
    }

    @Test
    public void shouldInstantiateWithDefaultGroups() {
        Assert.assertEquals(logParser.groups.length, 10);
    }

    @Ignore //ignored for file system dependencies
    @Test
    public void shouldParseTheFileIntoEmails() throws IOException {
        String filePath = "/Users/shula0/dev/jobvite/test/test.txt";
        logParser.readFile(filePath);

        Assert.assertEquals(logParser.emails.size(), 3);

        logParser.assignGroups();
    }

    @Ignore  //ignored for file system dependencies
    @Test
    public void runTheExample() throws IOException {
        for (int i = logParser.groupCount - 1; i > -1; i--) {
            logParser.removeGroup(i);
        }
        logParser.emails.clear();

        String filePath = "/Users/shula0/dev/jobvite/test/example.txt";
        logParser.readFile(filePath);


        logParser.assignGroups();
    }

    @Test
    public void shouldCreateGroup() {

        for (int i = logParser.groupCount - 1; i > -1; i--) {
            logParser.removeGroup(i);
        }

        Assert.assertEquals(logParser.groupCount, 0);
        String group = "blah";
        logParser.addGroup(group);
        Assert.assertEquals(logParser.groupCount, 1);
        Assert.assertEquals(logParser.groups[logParser.groupCount - 1], group);
    }

    @Test
    public void shouldRemoveGroup() {
        logParser.removeGroup(1);

        Assert.assertTrue(logParser.groupCount == 2);
        for (int i = 0; i < logParser.groupCount; i++) {
            assert !group2.equals(logParser.groups[i]);
        }
        Assert.assertNull(logParser.groups[logParser.groupCount]);
    }

    @Test
    public void shouldGrowGroupsIfOutOfSpace() {
        logParser.grow();
        Assert.assertEquals(logParser.groups.length, 20);

    }

    @Test
    public void shouldMergeTwoGroups() {
        logParser.mergeGroups(0, 1);

        Assert.assertEquals(logParser.groupCount, 2);
        Assert.assertEquals(logParser.groups[logParser.groupCount - 1], group1.concat(" ").concat(group2));
    }

    @Test
    public void shouldAddToGroup() {
        String participant = "drew";
        logParser.addToGroup(2, participant);

        Assert.assertTrue(logParser.groups[2].contains(participant));
        Assert.assertFalse(logParser.groups[0].contains(participant));
    }

    @Test
    public void shouldFindGroupMembership() {
        int groupFound = logParser.findGroupMembership("bleh");
        int groupNotFound = logParser.findGroupMembership("foo");

        Assert.assertTrue(groupFound > -1);
        Assert.assertTrue(groupNotFound == -1);
    }

    @Test
    public void shouldReAssignGroupMembership() {
        logParser.reassignMembership(1, "blah");
        Assert.assertTrue(logParser.participants.get("blah") == 1);
    }
}
