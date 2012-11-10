Private Sub Command1_Click()
    Mandelbrot ZoomXMax, ZoomXMin, ZoomYMax, ZoomYMin
End Sub

Private Sub Command2_Click()
    Shell "c:\windows\winfile.exe"
End Sub

Private Sub Command3_Click()
End Sub

Private Sub Form_Load()

    Width_of_Pic = Form1.ScaleWidth
    Height_of_Pic = Form1.ScaleHeight

    ReDim Pic(Width_of_Pic, Height_of_Pic)
    DownOrUp = 0

    ZoomXMax = 1
    ZoomXMin = -2
    ZoomYMax = 1.5
    ZoomYMin = -1.5

    OpenBookmark

    AddToHistory = 1
    HistoryNow = 200
    Iterations = 32

    Load Form5
    Load Form4
    Load Form3
    Load Form2
    
    DefaultColors
End Sub

Private Sub Form_MouseDown(Button As Integer, Shift As Integer, x As Single, Y As Single)
    Delete = 0
    DownOrUp = 1
    FirstX = x
    FirstY = Y
End Sub


Private Sub Form_MouseMove(Button As Integer, Shift As Integer, x As Single, Y As Single)
    Dim LorR As Integer
    Dim j As Integer
    Color = Form1.BackColor
    If x <= Form1.ScaleWidth And Y <= ScaleHeight And x >= 0 And Y >= 0 Then
        If Delete = 1 Then
    
            '-----------------------------------------------------------
            'Line (Temp(1), Temp(2))-(Temp(1), Temp(4)), Color
            LorR = 1
            If temp(2) > temp(4) Then LorR = -1
            For j = temp(2) To temp(4) Step LorR
                PSet (temp(1), j), Pic(temp(1), j)
            Next j
            'Line (Temp(1), Temp(4))-(Temp(3), Temp(4)), Color
            LorR = 1
            If temp(1) > temp(3) Then LorR = -1
            For j = temp(1) To temp(3) Step LorR
                PSet (j, temp(4)), Pic(j, temp(4))
            Next j
            'Line (Temp(3), Temp(4))-(Temp(3), Temp(2)), Color
            LorR = 1
            If temp(4) > temp(2) Then LorR = -1
            For j = temp(4) To temp(2) Step LorR
                PSet (temp(3), j), Pic(temp(3), j)
            Next j
            'Line (Temp(3), Temp(2))-(Temp(1), Temp(2)), Color
            LorR = 1
            If temp(3) > temp(1) Then LorR = -1
            For j = temp(3) To temp(1) Step LorR
                PSet (j, temp(2)), Pic(j, temp(2))
            Next j
    '-----------------------------------------------------------
        End If
        
        If DownOrUp > 0 Then
            Line (FirstX, FirstY)-(FirstX, Y)
            Line (FirstX, Y)-(x, Y)
            Line (x, Y)-(x, FirstY)
            Line (x, FirstY)-(FirstX, FirstY)
            temp(1) = FirstX
            temp(2) = FirstY
            temp(3) = x
            temp(4) = Y
        End If

        Delete = 1
    End If
End Sub


Private Sub Form_MouseUp(Button As Integer, Shift As Integer, x As Single, Y As Single)
    Dim Width1 As Double
    Dim Height1 As Double
    Dim TempXMin As Single
    Dim TempYMin As Single
    Dim Color
    Color = Form1.ForeColor
    DownOrUp = 0
        Line (temp(1), temp(2))-(temp(1), temp(4)), Color
        Line (temp(1), temp(4))-(temp(3), temp(4)), Color
        Line (temp(3), temp(4))-(temp(3), temp(2)), Color
        Line (temp(3), temp(2))-(temp(1), temp(2)), Color

    Width1 = (ZoomXMax - ZoomXMin)
    Height1 = (ZoomYMax - ZoomYMin)
    
    If Width1 < 0 Then Width1 = Width1 * -1
    If Height1 < 0 Then Height1 = Height1 * -1

    TempXMin = ZoomXMin
    TempYMin = ZoomYMin

    If Width1 / Width_of_Pic > Height1 / Height_of_Pic Then
        ZoomXMin = (FirstX / Width_of_Pic) * Width1 + TempXMin
        ZoomXMax = (x / Width_of_Pic) * Width1 + TempXMin
        ZoomYMin = (FirstY / Height_of_Pic) * Height1 + TempYMin
        ZoomYMax = (Y / Height_of_Pic) * Width1 + TempYMin '***
    ElseIf Width1 / Width_of_Pic < Height1 / Height_of_Pic Then
        ZoomXMin = (FirstX / Width_of_Pic) * Width1 + TempXMin
        ZoomXMax = (x / Width_of_Pic) * Height1 + TempXMin '***
        ZoomYMin = (FirstY / Height_of_Pic) * Height1 + TempYMin
        ZoomYMax = (Y / Height_of_Pic) * Height1 + TempYMin
    Else
        ZoomXMin = (FirstX / Width_of_Pic) * Width1 + TempXMin
        ZoomXMax = (x / Width_of_Pic) * Width1 + TempXMin
        ZoomYMin = (FirstY / Height_of_Pic) * Height1 + TempYMin
        ZoomYMax = (Y / Height_of_Pic) * Height1 + TempYMin
    End If
    '------------
'    Debug.Print TempXMin
'    Debug.Print TempYMin
'    Debug.Print Width1
'    Debug.Print Height1
'    Debug.Print ZoomXMin
'    Debug.Print ZoomXMax
'    Debug.Print ZoomYMin
'    Debug.Print ZoomYMax
'    Debug.Print FirstX
'    Debug.Print x
'    Debug.Print FirstY
'    Debug.Print Y
'    Debug.Print "------------"
    '-------------

    Mandelbrot ZoomXMax, ZoomXMin, ZoomYMax, ZoomYMin
    ReDraw
End Sub


Private Sub Form_Resize()

    Width_of_Pic = Form1.ScaleWidth
    Height_of_Pic = Form1.ScaleHeight

    ReDim Pic(Width_of_Pic, Height_of_Pic)

    Mandelbrot ZoomXMax, ZoomXMin, ZoomYMax, ZoomYMin
    ReDraw

End Sub


Private Sub mnuAdd_Click()
    Dim j As Integer
    Dim i As Integer
    
    CurrentData = CurrentData + 1
    MaximumBookmarks = CurrentData
    Data(CurrentData).MaximumX = ZoomXMax
    Data(CurrentData).MinimumX = ZoomXMin
    Data(CurrentData).MaximumY = ZoomYMax
    Data(CurrentData).MinimumY = ZoomYMin
    For i = 1 To 100
        Data(CurrentData).UserColorsRed(i) = UserColorsRed(i)
        Data(CurrentData).UserColorsGreen(i) = UserColorsGreen(i)
        Data(CurrentData).UserColorsBlue(i) = UserColorsBlue(i)
        Data(CurrentData).IterationRange(i) = IterationRange(i)
    Next i
    
    MiniMandelbrot ZoomXMax, ZoomXMin, ZoomYMax, ZoomYMin
    
    For j = 0 To 79
        For i = 0 To 59
            Data(CurrentData).Preview(j, i) = TempBook.Preview(j, i)
        Next i
    Next j
    
End Sub

Private Sub mnuBack_Click()
    HistoryNow = CurrentHistory
    If HistoryNow = 200 Then HistoryNow = CurrentHistory
    
    If HistoryNow > 1 Then
        HistoryNow = HistoryNow - 1
    
        ZoomXMax = History(HistoryNow).MaximumX
        ZoomXMin = History(HistoryNow).MinimumX
        ZoomYMax = History(HistoryNow).MaximumY
        ZoomYMin = History(HistoryNow).MinimumY
    
        AddToHistory = 0
        Mandelbrot ZoomXMax, ZoomXMin, ZoomYMax, ZoomYMin
        AddToHistory = 1
        ReDraw
    End If
End Sub

Private Sub mnuColors_Click()
    Form1.Hide
    Form5.Show
End Sub

Private Sub mnuExit_Click()
    SaveBookmark
    End
End Sub


Private Sub mnuForward_Click()

End Sub

Private Sub mnuGo_Click()
    Form1.Hide
    Form2.Caption = "Bookmarks"
    Form2.Show
    HistoryOrBookmark = 0
    Form2.HScroll1_Change
    Form2.HScroll1.Value = 1
End Sub

Private Sub mnuGotoPlace_Click()
    Form1.Hide
    Form8.Show
End Sub

Private Sub mnuHelp_Click()
    Form1.Hide
    Form6.Show
End Sub

Private Sub mnuHistory_Click()
    Form1.Hide
    Form2.Caption = "History"
    Form2.Show
    HistoryOrBookmark = 1
    Form2.HScroll1_Change
    Form2.HScroll1.Value = 1
End Sub

Private Sub mnuIterations_Click()
    Form1.Hide
    Form3.Show
    Form3.txtIterations = Iterations
    Form3.IterationsScroll.Value = Iterations
End Sub

Private Sub mnuRedraw_Click()
    ReDraw
End Sub

Private Sub mnuSaveAs_Click()
    Form7.Text2 = Width_of_Pic
    Form7.Text3 = Height_of_Pic
    Form1.Hide
    Form7.Show
End Sub

Private Sub mnuStartOver_Click()
    Width_of_Pic = Form1.ScaleWidth
    Height_of_Pic = Form1.ScaleHeight

    ReDim Pic(Width_of_Pic, Height_of_Pic)

    ZoomXMax = 1
    ZoomXMin = -2
    ZoomYMax = 1.5
    ZoomYMin = -1.5

    Mandelbrot ZoomXMax, ZoomXMin, ZoomYMax, ZoomYMin
    ReDraw
End Sub


