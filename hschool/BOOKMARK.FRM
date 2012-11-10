Private Sub Command1_Click()
Dim x As Integer
Dim x2 As Integer
Dim y2 As Integer
If HistoryOrBookmark = 0 And HScroll1.Max > 1 Then
    For x = HScroll1.Value To HScroll1.Max
        Data(x).MaximumX = Data(x + 1).MaximumX
        Data(x).MinimumX = Data(x + 1).MinimumX
        Data(x).MaximumY = Data(x + 1).MaximumY
        Data(x).MinimumY = Data(x + 1).MinimumY
        For x2 = 0 To 79
            For y2 = 0 To 59
                Data(x).Preview(x2, y2) = Data(x + 1).Preview(x2, y2)
            Next y2
        Next x2
    Next x
    CurrentData = CurrentData - 1
    HScroll1.Max = HScroll1.Max - 1
    HScroll1_Change
ElseIf HistoryOrBookmark = 1 And HScroll1.Max > 1 Then
    For x = HScroll1.Value To HScroll1.Max - 1
        History(x).MaximumX = History(x + 1).MaximumX
        History(x).MinimumX = History(x + 1).MinimumX
        History(x).MaximumY = History(x + 1).MinimumY
        History(x).MinimumY = History(x + 1).MinimumY
        For x2 = 0 To 79
            For y2 = 0 To 59
                History(x).Preview(x2, y2) = History(x + 1).Preview(x2, y2)
            Next y2
        Next x2
    Next x
    CurrentHistory = CurrentHistory - 1
    HScroll1.Max = HScroll1.Max - 1
    HScroll1_Change
End If
End Sub

Private Sub Command2_Click()
    Dim i As Integer
    If HistoryOrBookmark = 0 Then
        ZoomXMax = Data(HScroll1.Value).MaximumX
        ZoomXMin = Data(HScroll1.Value).MinimumX
        ZoomYMax = Data(HScroll1.Value).MaximumY
        ZoomYMin = Data(HScroll1.Value).MinimumY
        For i = 1 To 100
            UserColorsRed(i) = Data(CurrentData).UserColorsRed(i)
            UserColorsGreen(i) = Data(CurrentData).UserColorsGreen(i)
            UserColorsBlue(i) = Data(CurrentData).UserColorsBlue(i)
            IterationRange(i) = Data(CurrentData).IterationRange(i)
        Next i
    ElseIf HistoryOrBookmark = 1 Then
        ZoomXMax = History(HScroll1.Value).MaximumX
        ZoomXMin = History(HScroll1.Value).MinimumX
        ZoomYMax = History(HScroll1.Value).MaximumY
        ZoomYMin = History(HScroll1.Value).MinimumY
    End If

    Mandelbrot ZoomXMax, ZoomXMin, ZoomYMax, ZoomYMin
    Form2.Hide
    Form1.Show
    ReDraw
    
End Sub

Private Sub Command3_Click()
    Form2.Hide
    Form1.Show
    ReDraw
End Sub


Sub HScroll1_Change()
    Dim x As Integer
    Dim Y As Integer

    If HistoryOrBookmark = 0 Then
        HScroll1.Max = CurrentData
        lblMaxX = Data(HScroll1.Value).MaximumX
        lblMinX = Data(HScroll1.Value).MinimumX
        lblMaxY = Data(HScroll1.Value).MaximumY
        lblMinY = Data(HScroll1.Value).MinimumY
        lblCurrent = HScroll1.Value & " of " & CurrentData
        MaximumBookmarks = CurrentData
        For x = 0 To 79
            For Y = 0 To 59
                Picture1.PSet (x, Y), Data(HScroll1.Value).Preview(x, Y)
            Next Y
        Next x
    ElseIf HistoryOrBookmark = 1 Then
        HScroll1.Max = CurrentHistory
        lblMaxX = History(HScroll1.Value).MaximumX
        lblMinX = History(HScroll1.Value).MinimumX
        lblMaxY = History(HScroll1.Value).MaximumY
        lblMinY = History(HScroll1.Value).MinimumY
        lblCurrent = HScroll1.Value & " of " & CurrentHistory
        For x = 0 To 79
            For Y = 0 To 59
                Picture1.PSet (x, Y), History(HScroll1.Value).Preview(x, Y)
            Next Y
        Next x
    End If

End Sub


