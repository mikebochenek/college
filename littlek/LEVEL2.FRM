
Dim QuestionNumberq As Integer
Dim LastScore As Integer
Sub RedrawLevel2()
Dim Width As Integer
Dim Height As Integer
Dim TempB As Integer
Width = picTemporary.ScaleWidth \ 2
Height = picTemporary.ScaleHeight \ 2

Dim TempQ As Integer
ReDrawGame

For TempQ = 1 To WhereApplePut
    TempB = BitBlt(picLarge.hDC, (CoordsX(TempQ) - Width) \ Screen.TwipsPerPixelX, (CoordsY(TempQ) - Height) \ Screen.TwipsPerPixelY, picGoodMask.ScaleWidth \ Screen.TwipsPerPixelX, picGoodMask.ScaleHeight \ Screen.TwipsPerPixelY, picGoodMask.hDC, 0, 0, SRCPAINT)
    TempB = BitBlt(picLarge.hDC, (CoordsX(TempQ) - Width) \ Screen.TwipsPerPixelX, (CoordsY(TempQ) - Height) \ Screen.TwipsPerPixelY, picTemporary.ScaleWidth \ Screen.TwipsPerPixelX, picTemporary.ScaleHeight \ Screen.TwipsPerPixelY, picTemporary.hDC, 0, 0, SRCAND)
Next TempQ

End Sub


Private Sub Command1_Click()
Randomize
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

'Label1 = "Click on an apple in the bin then click on circle where you want it to go.  Remember to look out for bad ones and to put them in order!"
level2game
Command1.Enabled = False

End Sub

Private Sub Command2_Click()
If QuestionNumberq = 5 And WhereApplePut = Question Then
    Command1.Enabled = True
    Label1 = "Congradulations! You Completed This Level!"
    Beep
    MsgBox "Congradulations! You Completed This Level!", 64, "CONGRADULATIONS!"
    Score = Score + 50
    Label3 = Score
        
    '-------------- here you are done the level.
    Unload Form8
    Form8.Show
    
    
ElseIf WhereApplePut = Question Then
    Beep
    MsgBox "Correct! You got it right!", 64, "CORRECT"
    Score = Score + 100
    
    QuestionNumberq = QuestionNumberq + 1
    LastScore = Score
    
    WhereApplePut = 0
    Call Command1_Click

ElseIf WhereApplePut < Question Then
    Beep
    MsgBox "You need more than that!", 64, "NOT ENOUGH"
    'If Score >= 10 Then Score = Score - 10
    Score = LastScore
ElseIf WhereApplePut > Question Then
    Beep
    MsgBox "You have too many!", 64, "TOO MANY"
    'If Score >= 10 Then Score = Score - 10
    Score = LastScore
End If
Label3 = Score
End Sub

Private Sub Command3_Click()

End Sub

Private Sub Command4_Click()
'---- BIG IF
If WhereApplePut > 0 Then

Dim Width As Integer
Dim Height As Integer
Dim TempB As Integer
Width = picTemporary.ScaleWidth \ 2
Height = picTemporary.ScaleHeight \ 2

Dim TempQ As Integer
ReDrawGame

For TempQ = 1 To WhereApplePut - 1
    TempB = BitBlt(picLarge.hDC, (CoordsX(TempQ) - Width) \ Screen.TwipsPerPixelX, (CoordsY(TempQ) - Height) \ Screen.TwipsPerPixelY, picGoodMask.ScaleWidth \ Screen.TwipsPerPixelX, picGoodMask.ScaleHeight \ Screen.TwipsPerPixelY, picGoodMask.hDC, 0, 0, SRCPAINT)
    TempB = BitBlt(picLarge.hDC, (CoordsX(TempQ) - Width) \ Screen.TwipsPerPixelX, (CoordsY(TempQ) - Height) \ Screen.TwipsPerPixelY, picTemporary.ScaleWidth \ Screen.TwipsPerPixelX, picTemporary.ScaleHeight \ Screen.TwipsPerPixelY, picTemporary.hDC, 0, 0, SRCAND)
Next TempQ

If Score >= DefScore Then Score = Score - DefScore
Label3 = Score
MouseClicked = 0
WhereApplePut = WhereApplePut - 1

For TempQ = 24 To 0 Step -1
    If picApple(TempQ).Visible = False Then
        picApple(TempQ).Visible = True
        Exit For
    End If
Next TempQ

End If
End Sub

Private Sub Form_Load()
Form8.Caption = ProgTitle & " - Level " & Str(LevelNumber)
Form8.BackColor = Col(Setup.GameBack)
Form8.ForeColor = Col(Setup.GameFore)
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
    Case 2
        WhereApplePut = 0
        WhichSet = 1
End Select
MouseClicked = 0
CurrentAppleKind = 1
Question = 0
Score = 0
End Sub


Private Sub mnuAbout_Click()
Form5.Show
End Sub

Private Sub mnuexitgame_Click()
End
End Sub

Private Sub mnuLevelChange_Click()
CurrentLevelNumber = 2
Form8.Hide
Form3.Show
'Unload Form8
End Sub

Private Sub mnuNewGame_Click()
Unload Form8
Form8.Show

End Sub

Private Sub mnuSetup_Click()
Form6.Show
'Unload Form8
End Sub


Private Sub mnuViewHS_Click()
Form7.Show
End Sub

Private Sub picApple_MouseDown(Index As Integer, Button As Integer, Shift As Integer, X As Single, Y As Single)
Dim CounterJ As Integer
Dim JJ As Integer
Dim HeightN As Integer
Dim WidthN As Integer
WidthN = picTemporary.ScaleWidth \ 2
HeightN = picTemporary.ScaleHeight \ 2

'picApple(Index).Visible = True
'picApple(Index).Enabled = True

picTemporary.Picture = picApple(Index).Picture

If Button = 2 And WhereApplePut <= 15 And L2Bad(Index) = False Then
    CounterJ = 0
    JJ = 0
    Do
        If L2Bad(0 + JJ) = False And picApple(0 + JJ).Visible = True Then
        'If picApple(WhereApplePut + JJ).Picture <> picGBad.Picture Then
            CounterJ = CounterJ + 1
            picApple(0 + JJ).Visible = False
        End If
        JJ = JJ + 1
    Loop Until CounterJ = 5
    
    For JJ = 1 To 5
        TempB = BitBlt(picLarge.hDC, (CoordsX(WhereApplePut + 1) - WidthN) \ Screen.TwipsPerPixelX, (CoordsY(WhereApplePut + 1) - HeightN) \ Screen.TwipsPerPixelY, picGoodMask.ScaleWidth \ Screen.TwipsPerPixelX, picGoodMask.ScaleHeight \ Screen.TwipsPerPixelY, picGoodMask.hDC, 0, 0, SRCPAINT)
        TempB = BitBlt(picLarge.hDC, (CoordsX(WhereApplePut + 1) - WidthN) \ Screen.TwipsPerPixelX, (CoordsY(WhereApplePut + 1) - HeightN) \ Screen.TwipsPerPixelY, picTemporary.ScaleWidth \ Screen.TwipsPerPixelX, picTemporary.ScaleHeight \ Screen.TwipsPerPixelY, picTemporary.hDC, 0, 0, SRCAND)
    
        
        Score = Score + DefScore
        Label3 = Score
        WhereApplePut = WhereApplePut + 1
    Next JJ
    
    MouseClicked = 0
ElseIf Button = 1 Then
    picTemporary.Picture = picApple(Index).Picture
    MouseClicked = 1

    ClicType.applebad = L2Bad(Index)
    For jfk = 0 To 24
        picApple(jfk).Enabled = False
    Next jfk
    ClicType.applenum = Index + 1
    picApple(Index).Visible = False

    
End If

End Sub


Private Sub PicGarbage_Click()
If MouseClicked = 1 Then
If ClicType.applebad = True Then
    Score = Score + DefScore
    Label3 = Score
    MouseClicked = 0
    'If (picApple(0).Visible = False) And (picApple(1).Visible = False) And (picApple(2).Visible = False) And (picApple(3).Visible = False) And (picApple(4).Visib