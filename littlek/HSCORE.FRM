
Private Sub Command1_Click()
Unload Me
End Sub

Private Sub Form_Load()
For tempx1 = 0 To 13
    Label1(tempx1).ForeColor = Col(Setup.HScoreFore)
    Label1(tempx1).BackColor = Col(Setup.HScoreBack)
Next tempx1
For tempx1 = 0 To 9
    Label2(tempx1).ForeColor = Col(Setup.HScoreName)
    Label2(tempx1).BackColor = Col(Setup.HScoreBack)
Next tempx1
For tempx1 = 0 To 9
    Label3(tempx1).ForeColor = Col(Setup.HScoreScore)
    Label3(tempx1).BackColor = Col(Setup.HScoreBack)
Next tempx1
Form7.BackColor = Col(Setup.HScoreBack)
Form7.Caption = ProgTitle & " - High Scores"
Label1(0).Caption = ProgTitle & " - High Scores For " & Date
Open HighScoreFileName For Random Access Read As #1 Len = 5000
    Get #1, , HScore
Close #1
For tempx1 = 0 To 9
    Label2(tempx1).Caption = HScore.Names(tempx1)
    Label3(tempx1).Caption = HScore.Scores(tempx1)
Next tempx1
End Sub


Private Sub Label1_Click(Index As Integer)

End Sub


