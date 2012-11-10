Private Sub Command1_Click()
    Iterations = Val(txtIterations)
    Form3.Hide
    Form1.Show
    Mandelbrot ZoomXMax, ZoomXMin, ZoomYMax, ZoomYMin
    ReDraw
End Sub

Private Sub Iterations_Change()
    'txtIterations = Iterations.Value
End Sub


Private Sub Command2_Click()
    Form3.Hide
    Form1.Show
    ReDraw
End Sub

Private Sub Command3_Click()

End Sub

Private Sub IterationsScroll_Change()
    txtIterations = IterationsScroll.Value
End Sub

Private Sub LoadForm3_Click()
    txtIterations = Iterations
    IterationsScroll.Value = Iterations
End Sub


