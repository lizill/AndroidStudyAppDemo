package com.example.studyapp.ui.chart;

import com.example.studyapp.FirstActivity;

public class Env {
    public static String daysTotalurl = "https://dong0110.com/chatphp/chart/DayStudyTime.php?userID=%s";
    public static String fetchURL = "https://dong0110.com/chatphp/timer/FetchTime.php?userID=%s&study_date=%s&study_subject=%s";
    public static String ReSaveURL = "https://dong0110.com/chatphp/timer/ReSaveTime.php";
    public static String SaveURL = "https://dong0110.com/chatphp/timer/SaveTime.php";
    public static String BeginEndURL = "https://dong0110.com/chatphp/timer/BeginEndTime.php";
    public static String totalURL2 = "https://dong0110.com/chatphp/timer/TotalTime2.php?userID=%s&study_date=%s";
    public static String BarchartURL = "https://dong0110.com/chatphp/chart/RequireBarchart.php?userID=%s&study_date=%s";
    public static String TimelineURL = "https://dong0110.com/chatphp/chart/RequireTimeline.php?userID=%s&study_date=%s";
    public static String InfoURL = "https://dong0110.com/chatphp/chart/RequireInfo.php?userID=%s&study_date=%s";
    public static String subjectNameURL = "https://dong0110.com/chatphp/timer/SubjectName.php?userID=%s&study_date=%s";
    public static String PlusSubjectURL = "https://dong0110.com/chatphp/timer/SendSubject.php";
    public static String DeleteSubjectURL = "https://dong0110.com/chatphp/timer/DeleteSubject.php";
}

