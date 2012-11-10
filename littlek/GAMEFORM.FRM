
Sub PrintPerfection()
Load frmPerfect
Unload frmPerfect

End Sub


Sub RedrawLevel1()
Dim TempQ As Integer
ReDrawGame

Dim Width As Integer
Dim Height As Integer
Dim TempB As Integer

Width = picTemporary.ScaleWidth \ 2
Height = picTemporary.ScaleHeight \ 2


For TempQ = 1 To WhereApplePut
    TempB = BitBlt(picLarge.hDC, (CoordsX(TempQ) - Width) \ Screen.TwipsPerPixelX, (CoordsY(TempQ) - Height) \ Screen.TwipsPerPixelY, picGoodMask.ScaleWidth \ Screen.TwipsPerPixelX, picGoodMask.ScaleHeight \ Screen.TwipsPerPixelY, picGoodMask.hDC, 0, 0, SRCPAINT)
    TempB = BitBlt(picLarge.hDC, (CoordsX(TempQ) - Width) \ Screen.TwipsPerPixelX, (CoordsY(TempQ) - Height) \ Screen.TwipsPerPixelY, picTemporary.ScaleWidth \ Screen.TwipsPerPixelX, picTemporary.ScaleHeight \ Screen.TwipsPerPixelY, picTemporary.hDC, 0, 0, SRCAND)
Next TempQ

End Sub


Private Sub Command1_Click()
If WhereApplePut = 0 Then
    ReDrawGame
End If

'X = BitBlt(picLarge.hDC, picLarge.WIDTH \ BORDER, picLarge.HEIGHT \ BORDER, picTemp.WIDTH, picTemp.HEIGHT, picTemp.hDC, 0, 0, SRCCOPY)
'X = BitBlt(picLarge.hDC, picLarge.WIDTH \ BORDER, picLarge.HEIGHT \ BORDER + BORDER + picTemp.HEIGHT, picTemp.WIDTH, picTemp.HEIGHT, picTemp.hDC, 0, 0, SRCCOPY)
'X = BitBlt(picLarge.hDC, picLarge.WIDTH \ BORDER + BORDER + picTemp.WIDTH, picLarge.HEIGHT \ BORDER + BORDER + picTemp.HEIGHT, picTemp.WIDTH, picTemp.HEIGHT, picTemp.hDC, 0, 0, SRCCOPY)
'X = BitBlt(picLarge.hDC, picLarge.WIDTH \ BORDER + BORDER + picTemp.WIDTH, picLarge.HEIGHT \ BORDER, picTemp.WIDTH, picTemp.HEIGHT, picTemp.hDC, 0, 0, SRCCOPY)

CoordsX(1) = 615
CoordsY(1) = 1500
CoordsX(2) = 2310
CoordsY(2) = 1515
CoordsX(3) = 2295
CoordsY(3) = 555
CoordsX(4) = 630
CoordsY(4) = 555
CoordsX(5) = 1470
CoordsY(5) = 1020
CoordsX(6) = 3480
CoordsY(6) = 1485
CoordsX(7) = 5175
CoordsY(7) = 1500
CoordsX(8) = 5175
CoordsY(8) = 555
CoordsX(9) = 3480
CoordsY(9) = 555
CoordsX(10) = 4335
CoordsY(10) = 1020
CoordsX(11) = 615
CoordsY(11) = 3510
CoordsX(12) = 2310
CoordsY(12) = 3510
CoordsX(13) = 2310
CoordsY(13) = 2535
CoordsX(14) = 615
CoordsY(14) = 2580
CoordsX(15) = 1470
CoordsY(15) = 3060
CoordsX(16) = 3465
CoordsY(16) = 3510
CoordsX(17) = 5160
CoordsY(17) = 3510
CoordsX(18) = 5160
CoordsY(18) = 2535
CoordsX(19) = 3465
CoordsY(19) = 2580
CoordsX(20) = 4335
CoordsY(20) = 3045

GenerateApples CurrentAppleKind

Label1 = "Click on an apple in the bin then click on circle where you want it to go.  Remember to look out for bad ones and to put them in order!"
Command1.Enabled = False
Picture1.Visible = False

End Sub





Private Sub Command2_Click()
'Start Next Kind Of Apple
If Score >= DefScore * (25 * 2 + 1) And WhereApplePut = 20 Then
    '----------- done 2 x 20
    Call PrintPerfection
    Unload Form4
    Form4.Show
ElseIf WhereApplePut = 20 Then
    WhereApplePut = 0
    ReDrawGame
    CurrentAppleKind = CurrentAppleKind + 1
    If CurrentAppleKind = 4 Then CurrentAppleKind = 1
    Command1.Enabled = True
    Picture1.Visible = True
Else
    MsgBox "Sorry, but you need 20 apples"
End If
End Sub

Private Sub Command3_Click()

End Sub

Private Sub Command4_Click()

End Sub

Private Sub Form_Load()
Form4.Caption = ProgTitle & " - Level " & Str(LevelNumber)
Form4.BackColor = Col(Setup.GameBack)
Form4.ForeColor = Col(Setup.GameFore)
Frame1.BackColor = Col(Setup.GameBack)
Frame1.ForeColor = Col(Setup.GameFore)
Frame2.BackColor = Col(Setup.GameBack)
Frame2.ForeColor = Col(Setup.GameFore)
Label1.BackColor = Col(Setup.GameBack)
Label1.ForeColor = Col(Setup.GameFore)
Label2.BackColor = Col(Setup.GameBack)
Label3.BackColor = Col(Setup.GameBack)
Label3.ForeColor = Col(Setup.GameFore)
Label3 = "0"
Label1 = ""
Select Case LevelNumber
    Case 1
        WhereApplePut = 0
        WhichSet = 1
        
        'picApple(0).Picture = PicGreen.Picture
        'picApple(1).Picture = PicGreen.Picture
        'picApple(2).Picture = PicGreen.Picture
        'picApple(3).Picture = PicGreen.Picture
        'picApple(4).Picture = picGBad.Picture
End Select
MouseClicked = 0
CurrentAppleKind = 1

App.HelpFile = "c:\vb40\littlek\main.hlp"
'App.HelpFile = "main.hlp"
Score = 0
End Sub


Private Sub mnuAbout_Click()
Form5.Show
End Sub


Private Sub mnuExit_Click()
End
End Sub

Private Sub mnuHowTo_Click()
SendKeys "{F1}", True

End Sub

Private Sub mnuHS_Click()
Form7.Show
End Sub

Private Sub mnuLevelChange_Click()
CurrentLevelNumber = 1
Form4.Hide
Form3.Show
'Unload Form4
End Sub

Private Sub mnuNew_Click()
Unload Form4
Form4.Show
End Sub

Private Sub mnuSetup_Click()
Form6.Show
'Unload Form4
End Sub


Private Sub picApple_Click(Index As Integer)
picTemporary.Picture = picApple(Index).Picture
MouseClicked = 1

'ClicType.applenum = 1
'ClicType.applebad = AppleBad1
'picApple(Index).Visible = False
'MouseClicked = 1
Select Case Index
    Case 0
        ClicType.applebad = AppleBad1
    Case 1
        ClicType.applebad = AppleBad2
    Case 2
        ClicType.applebad = AppleBad3
    Case 3
        ClicType.applebad = AppleBad4
    Case 4
        ClicType.applebad = AppleBad5
End Select
For jfk = 0 To 4
    picApple(jfk).Enabled = False
Next jfk
ClicType.applenum = Index + 1
picApple(Index).Visible = False
MouseClicked = 1
End Sub

Private Sub PicGarbage_Click()
If MouseClicked = 1 Then
If ClicType.applebad = True Then
    Score = Score + DefScore
    Label3 = Score
    MouseClicked = 0
    If (picApple(0).Visible = False) And (picApple(1).Visible = False) And (picApple(2).Visible = False) And (picApple(3).Visible = False) And (picApple(4).Visible = False) Then
        Call Command1_Click
        
        'Start Next Kind Of Apple
        If WhereApplePut = 20 Then
            WhereApplePut = 0
            ReDrawGame
            CurrentAppleKind = CurrentAppleKind + 1
            If CurrentAppleKind = 4 Then CurrentAppleKind = 1
        End If
    End If
ElseIf ClicType.applebad = False Then
    Beep
    MsgBox "You can't put a good apple in the garbage now can you?", 64, "GOOD APPLE IN GARBAGE?"
    Label1 = "You can't put a good apple in the garbage now can you?"
    Timer1.Enabled = True
    If Score >= 10 Then
        Score = Score - 10
    ElseIf Score > 0 Then
        Score = Score - 1
    End If
    Label3 = Score
    MouseClicked = 0
    picApple(ClicType.applenum - 1).Visible = True
End If
For jfk = 0 To 4
    picApple(jfk).Enabled = True
Next jfk
End If

End Sub

Private Sub picLarge_MouseDown(Button As Integer, Shift As Integer, X As Single, Y As Single)

'-----------------KEEP THIS----------------------
'Static yy As Integer
'yy = yy + 1
'Debug.Print "coordsX(" & yy & ") = " & X
'Debug.Print "coordsY(" & yy & ") = " & Y
'------------------------------------------------

Dim Width As Integer
Dim Height As Integer
Dim TempB As Integer

Width = picTemporary.ScaleWidth \ 2
Height = picTemporary.ScaleHeight \ 2
If X + Width > CoordsX(WhereApplePut + 1) And X - Width < CoordsX(WhereApplePut + 1) And Y + Height > CoordsY(WhereApplePut + 1) And Y - Height < CoordsY(WhereApplePut + 1) Then
    If MouseClicked = 1 Then
        If ClicType.applebad Then
            Beep
            MsgBox "You can't put a bad apple on the game field!", 64, "BAD APPLE!"
            Label1 = "You can't put a bad apple on the game field!"
            Timer1.Enabled = True
            picApple(ClicType.applenum - 1).Visible = True
            MouseClicked = 0
            If Score >= 10 Then
                Score = Score - 10
            ElseIf Score > 0 Then
                Score = Score - 1
            End If
            Label3 = Score
        Else
        
            TempB = BitBlt(picLarge.hDC, (CoordsX(WhereApplePut + 1) - Width) \ Screen.TwipsPerPixelX, (CoordsY(WhereApplePut + 1) - Height) \ Screen.TwipsPerPixelY, picGoodMask.ScaleWidth \ Screen.TwipsPerPixelX, picGoodMask.ScaleHeight \ Screen.TwipsPerPixelY, picGoodMask.hDC, 0, 0, SRCPAINT)
            TempB = BitBlt(picLarge.hDC, (CoordsX(WhereApplePut + 1) - Width) \ Screen.TwipsPerPixelX, (CoordsY(WhereApplePut + 1) - Height) \ Screen.TwipsPerPixelY, picTemporary.ScaleWidth \ Screen.TwipsPerPixelX, picTemporary.ScaleHeight \ Screen.TwipsPerPixelY, picTemporary.hDC, 0, 0, SRCAND)
    
            Score = Score + DefScore
            Label3 = Score
            MouseClicked = 0
            WhereApplePut = WhereApplePut + 1
                      
            'Finished Rack
            If (picApple(0).Visible = False) And (picApple(1).Visible = False) And (picApple(2).Visible = False) And (picApple(3).Visible = False) And (picApple(4).Visible = False) Then
                Call Command1_Click
            End If
        End If
        MouseClicked = 0
        For jfk = 0 To 4
            picApple(jfk).Enabled = True
        Next jfk
    End If
End If

End Sub

Private Sub Picture2_Click()
'Start Next Kind Of Apple
If Score >= DefScore * (25 * 2 + 1) And WhereApplePut = 20 Then
    '----------- done 2 x 20
    Call PrintPerfection
    Unload Form4
    Form4.Show
ElseIf WhereApplePut = 20 Then
    WhereApplePut = 0
    ReDrawGame
    CurrentAppleKind = CurrentAppleKind + 1
    If CurrentAppleKind = 4 Then CurrentAppleKind = 1
    Command1.Enabled = True
    Picture1.Visible = True
Else
    MsgBox "Sorry, but you need 20 apples"
End If

End Sub

Private Sub picOK_Click()
'Start Next Kind Of Apple
If Score >= DefScore * (25 * 2 + 1) And WhereApplePut = 20 Then
    '----------- done 2 x 20
    Call PrintPerfection
    Unload Form4
    Form4.Show
ElseIf WhereApplePut = 20 Then
    WhereApplePut = 0
    ReDrawGame
    CurrentAppleKind = CurrentAppleKind + 1
    If CurrentAppleKind = 4 Then CurrentAppleKind = 1
    Command1.Enabled = True
    Picture1.Visible = True
Else
    MsgBox "Sorry, but you need 20 apples"
End If

End Sub

Private Sub Timer1_Timer()
'Label1 = "Click on an apple in the bin then click on circle where you want it to go.  Remember to look out for bad ones and to put them in order!"
Label1 = "Click on an apple then put it on play field in the correct order.  Be sure to put rotten apples in the garbage."
Timer1.Enabled = False
End Sub


