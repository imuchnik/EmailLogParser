package com.jobvite;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class LogParser {

    final String WORD_DELIMETER = " ";

    ArrayList<String> emails;
    String[] groups;
    HashMap<String, Integer> participants;
    int groupCount = 0;

    public LogParser() {
        this.emails = new ArrayList<String>();
        this.groups = new String[10];
        this.participants = new HashMap<String, Integer>();
    }

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.print("the program expects the file path");
            System.exit(1);
        }
        String filePath = args[0];
        LogParser parser = new LogParser();
        try {
            parser.readFile(filePath);
            parser.assignGroups();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(1);
        } catch (IOException t) {
            t.printStackTrace();
            System.exit(1);
        }

    }

    //workhorse, this is where all the logic happens
    public void assignGroups() {
        for (int i = 0; i < emails.size() - 1; i++) {

            String[] emailRecipients = Arrays.copyOf(emails.get(i).split(WORD_DELIMETER), emails.get(i).split(WORD_DELIMETER).length);

            for (int x = 1; x < emailRecipients.length; x++) {
                //4 possible paths.
                // 1. Both recipients are already assigned to the same group -> next
                // 1. Both recipients are already assigned to different groups -> merge groups
                // 2.Both are new ->create new group and add it.
                // 3.a is new and b is assigned -> add a to group of b;

                int groupA = findGroupMembership(emailRecipients[x]);
                int groupB = findGroupMembership(emailRecipients[x - 1]);

                //already belong to the same group  -case 1
                if (groupA == groupB && (groupA > -1 && groupB > -1)) {
                    continue;
                }
                //both belong to different groups -case 2
                if (groupA > -1 && groupB > -1) {
                    mergeGroups(groupA, groupB);
                }
                //do not belong to any groups yet  case 3
                if (groupA == -1 && groupB == -1) {
                    String newGroup = emailRecipients[x - 1].concat(WORD_DELIMETER).concat(emailRecipients[x]);
                    addGroup(newGroup);
                }
                //First is in the group and second is not  -case 4
                if (groupA > -1 && groupB == -1) {
                    addToGroup(groupA, emailRecipients[x - 1]);
                }
                if (groupA == -1 && groupB > -1) {
                    addToGroup(groupB, emailRecipients[x]);
                }
            }
        }
        if (groupCount > 0) {
            String isPlural = groupCount > 1 ? "There are" : "There is";
            System.out.println(String.format("%s %s groups", isPlural, groupCount));

            for (int i = 0; i < groupCount; i++) {
                System.out.println(String.format("Group %s: %s", i + 1, groups[i]));
            }
        }
    }

    public void readFile(String aFileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(aFileName));
        try {
            String line;
            while ((line = br.readLine()) != null) {
                emails.add(line);
            }
        } finally {
            br.close();
        }
    }

    void addGroup(String group) {
        if (groupCount == groups.length) {
            grow();
        }
        groups[groupCount] = group;
        reassignMembership(groupCount, group);
        groupCount++;
    }

    void reassignMembership(int groupId, String group) {
        String[] groupMembers = group.split(WORD_DELIMETER);

        for (String groupMember : groupMembers) {
            participants.put(groupMember, groupId);
        }
    }

    void removeGroup(int i) {
        groups[i] = groups[groupCount - 1];
        reassignMembership(i, groups[groupCount - 1]);
        groups[groupCount - 1] = null;
        groupCount--;
    }

    public void grow() {
        groups = Arrays.copyOf(groups, groups.length * 2);
    }

    int findGroupMembership(String participant) {
        return participants.get(participant) != null ? participants.get(participant) : -1;
    }

    void addToGroup(int groupId, String participant) {
        String newGroup = groups[groupId].concat(WORD_DELIMETER).concat(participant);
        groups[groupId] = newGroup;
        participants.put(participant, groupId);
    }

    void mergeGroups(int firstGroup, int secondGroup) {
        String newGroup = groups[firstGroup].concat(WORD_DELIMETER).concat(groups[secondGroup]);
        removeGroup(firstGroup);
        removeGroup(secondGroup);
        addGroup(newGroup);
    }
}
