Private Sub Command1_Click()
    Form5.Hide
    Form1.Show
    Mandelbrot ZoomXMax, ZoomXMin, ZoomYMax, ZoomYMin
    ReDraw
End Sub


Private Sub Command2_Click()
    Form5.Hide
    Form1.Show
    DefaultColors
    Mandelbrot ZoomXMax, ZoomXMin, ZoomYMax, ZoomYMin
    ReDraw
End Sub


Private Sub Command3_Click()
    Dim x As Integer
    
    If HScroll4.Value > 1 Then
        If Val(txtTo) > IterationRange(HScroll4.Value - 1) Then
            IterationRange(HScroll4.Value) = Val(txtTo)
            For x = HScroll4.Value + 1 To Val(txtStages)
                IterationRange(x) = Int(x * 8)
            Next x
        End If
    Else
        IterationRange(HScroll4.Value) = Val(txtTo)
        For x = HScroll4.Value + 1 To Val(txtStages)
            IterationRange(x) = Int(x * 8)
        Next x
    End If
End Sub

Private Sub HScroll1_Change()

    lblR = HScroll1.Value
    lblG = HScroll2.Value
    lblB = HScroll3.Value
    Picture1.BackColor = RGB(lblR, lblG, lblB)
    UserColorsRed(HScroll4.Value) = lblR
    UserColorsGreen(HScroll4.Value) = lblG
    UserColorsBlue(HScroll4.Value) = lblB

End Sub


Private Sub HScroll2_Change()
    lblR = HScroll1.Value
    lblG = HScroll2.Value
    lblB = HScroll3.Value
    Picture1.BackColor = RGB(lblR, lblG, lblB)
    UserColorsRed(HScroll4.Value) = lblR
    UserColorsGreen(HScroll4.Value) = lblG
    UserColorsBlue(HScroll4.Value) = lblB
End Sub

Private Sub HScroll3_Change()
    lblR = HScroll1.Value
    lblG = HScroll2.Value
    lblB = HScroll3.Value
    Picture1.BackColor = RGB(lblR, lblG, lblB)
    UserColorsRed(HScroll4.Value) = lblR
    UserColorsGreen(HScroll4.Value) = lblG
    UserColorsBlue(HScroll4.Value) = lblB
End Sub

Private Sub HScroll4_Change()
    Dim ii As Integer
    Dim jj As Integer
    lblStage = HScroll4.Value & " of " & HScroll4.Max
    lblCurrent = "Stage " & HScroll4.Value & " of " & HScroll4.Max
    
    jj = UserColorsRed(HScroll4.Value)
    Do While jj > 255
        jj = jj - 255
    Loop
    Do While jj < 0
        jj = jj + 255
    Loop
    UserColorsRed(HScroll4.Value) = jj
    
    jj = UserColorsGreen(HScroll4.Value)
    Do While jj > 255
        jj = jj - 255
    Loop
    Do While jj < 0
        jj = jj + 255
    Loop
    UserColorsGreen(HScroll4.Value) = jj
    
    jj = UserColorsBlue(HScroll4.Value)
    Do While jj > 255
        jj = jj - 255
    Loop
    Do While jj < 0
        jj = jj + 255
    Loop
    UserColorsBlue(HScroll4.Value) = jj
    
    HScroll1.Value = UserColorsRed(HScroll4.Value)
    HScroll2.Value = UserColorsGreen(HScroll4.Value)
    HScroll3.Value = UserColorsBlue(HScroll4.Value)
    lblR = HScroll1.Value
    lblG = HScroll2.Value
    lblB = HScroll3.Value
    
    If HScroll4.Value = 1 Then
        lblFrom = 0
        txtTo = IterationRange(1)
    Else
        lblFrom = IterationRange(HScroll4.Value - 1) + 1
        txtTo = IterationRange(HScroll4.Value)
    End If
End Sub


Private Sub lblTo_Click()
End Sub

Private Sub txtStages_Change()
    Dim x As Integer
    If txtStages <= 100 Then
        HScroll4.Max = Val(txtStages)
    Else
        txtStages = 100
        HScroll4.Max = Val(txtStages)
    End If

    For x = 1 To Val(txtStages)
        IterationRange(x) = Int(x * 8)
    Next x

    HScroll4_Change

End Sub


