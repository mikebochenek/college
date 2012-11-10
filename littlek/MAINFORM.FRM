
Dim RegDate As String

Private Sub Command1_Click()
Timer1_Timer

End Sub


Private Sub Form_Load()
Form2.Caption = "Welcome to " & ProgTitle
RegDate = Date
RegDate = Left$(RegDate, 1)
If Val(RegDate) > 8 Then
    MsgBox "Your Shareware License Has Expired. You Must Pay The $50 Registration Fee.", 64, ProgTitle & " - No Cheating Allowed"
    Dim X()
    ReDim X(1000, 1000, 1000)
    End
End If

Open RegFileName For Random Access Read As #1 Len = 265
    Get #1, , Reg
Close #1
Open SetupFileName For Random Access Read As #1 Len = 1000
    Get #1, , Setup
Close #1
Label1 = Decrypt(Reg.RegName)
Label2 = Decrypt(Reg.RegNumber)
Label6 = ProgTitle & " Version " & VersionNumber
Label7 = "Copyright      1998 by " & Authors
Label9 = "Warning:  This computer program is protected by copyright law and international treaties.  Unauthorized reproduction or distribution of this program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under law."
Col(0) = &HFFFFFF
Col(1) = &HE0E0E0
Col(2) = &HC0C0FF
Col(3) = &HC0E0FF
Col(4) = &HC0FFFF
Col(5) = &HC0FFC0
Col(6) = &HFFFFC0
Col(7) = &HFFC0C0
Col(8) = &HFFC0FF
Col(9) = &HC0&
Col(10) = &H40C0&
Col(11) = &HC0C0&
Col(12) = &HC000&
Col(13) = &HC0C000
Col(14) = &HC00000
Col(15) = &HC000C0
Col(17) = &H80C0FF
Col(18) = &H8080FF
Col(19) = &H404040
Col(20) = &HC0C0C0
Col(21) = &H80FFFF
Col(22) = &H80FF80
Col(23) = &HFFFF80
Col(24) = &HFF8080
Col(25) = &HFF80FF
Col(26) = &H80&
Col(27) = &H4080&
Col(28) = &H8080&
Col(29) = &H8000&
Col(30) = &H808000
Col(31) = &H800000
Col(32) = &H800080
Col(16) = &H80FF&
Col(33) = &HFF&
Col(34) = &H0&
Col(35) = &H808080
Col(36) = &HFFFF&
Col(37) = &HFF00&
Col(38) = &HFFFF00
Col(39) = &HFF0000
Col(40) = &HFF00FF
Col(41) = &H40&
Col(42) = &H404080
Col(43) = &H4040&
Col(44) = &H4000&
Col(45) = &H404000
Col(46) = &H400000
Col(47) = &H400040
ColType = 1
'Kill SetupFileName
'Open SetupFileName For Random Access Write As #1 Len = 1000
'    Setup.TitleName = ProgTitle & " - Setup File"
'    Setup.DefaultLevelNum = 1
'    Setup.LevelSelectBack = 0
'    Setup.LevelSelectFore = 34
'    Setup.SetupBack = 0
'    Setup.SetupFore = 34
'    Setup.GameBack = 0
'    Setup.GameFore = 34
'    Setup.HScoreBack = 0
'    Setup.HScoreFore = 34
'    Setup.HScoreName = 34
'    Setup.HScoreScore = 34
'    Setup.L3AQuestions = 10
'    Setup.L3BQuestions = 10
'    Put #1, , Setup
'Close #1
'End
'Kill HighScoreFileName
'Open HighScoreFileName For Random Access Write As #1 Len = 5000
'    For tempx3 = 0 To 9
'        HScore.Names(tempx3) = "Blank"
'        HScore.Scores(tempx3) = 0
'    Next tempx3
'    Put #1, , HScore
'Close #1
'End
End Sub


Private Sub Timer1_Timer()
Timer1.Interval = 0
LevelNumber = Setup.DefaultLevelNum
Unload Form2
Select Case LevelNumber
    Case 1
        Form4.Show
    Case 2
        Form8.Show
    Case 3
        Form9.Show
    Case 4
        Form10.Show
End Select
End Sub


