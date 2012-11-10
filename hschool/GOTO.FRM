Private Sub Command1_Click()
    Unload Form8
    Form1.Show
    ReDraw
End Sub


Private Sub Command2_Click()
    Dim x As Integer
    x = 0
    If Text1 <> "" Then x = x + 1
    If Text2 <> "" Then x = x + 1
    If Text3 <> "" Then x = x + 1
    If Text4 <> "" Then x = x + 1
    
    If x = 4 Then
        ZoomXMax = Text1
        ZoomXMin = Text2
        ZoomYMax = Text3
        ZoomYMin = Text4
        Unload Form8
        Form1.Show
        Mandelbrot ZoomXMax, ZoomXMin, ZoomYMax, ZoomYMin
        ReDraw
    End If
End Sub

Private Sub Form_Load()
    Text1 = ZoomXMax
    Text2 = ZoomXMin
    Text3 = ZoomYMax
    Text4 = ZoomYMin
End Sub


