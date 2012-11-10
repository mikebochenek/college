Attribute VB_Name = "Module1"
Type Bookmark
    MaximumX As Double
    MinimumX As Double
    MaximumY As Double
    MinimumY As Double
    Preview(80, 60) As Long
    UserColorsRed(1 To 100) As Integer
    UserColorsGreen(1 To 100) As Integer
    UserColorsBlue(1 To 100) As Integer
    IterationRange(1 To 100) As Integer
End Type

Global Data(1 To 100) As Bookmark
Global CurrentData As Integer
Global History(1 To 100) As Bookmark
Global CurrentHistory As Integer
Global TempBook As Bookmark
Global DataNow As Integer
Global HistoryNow As Integer

Global Width_of_Pic As Integer
Global Height_of_Pic As Integer

Global Iterations As Integer

Global UserColorsRed(1 To 100) As Integer
Global UserColorsGreen(1 To 100) As Integer
Global UserColorsBlue(1 To 100) As Integer

Global IterationRange(1 To 100) As Integer
Global MaximumStages As Integer

Global FirstX As Single
Global FirstY As Single
Global SecondX As Single
Global SecondY As Single

Global DownOrUp As Integer
Global Delete As Integer

Global HistoryOrBookmark As Integer
Global AddToHistory As Integer
Global MaximumBookmarks As Integer

Global temp(1 To 4) As Single
Global Pic() As Long

Global XMAX As Double
Global XMIN As Double
Global YMIN As Double
Global YMAX As Double

Global ZoomXMax As Double
Global ZoomXMin As Double
Global ZoomYMax As Double
Global ZoomYMin As Double

Function AssignColor(ByVal Counter As Integer) As Long
    Dim temp As Long
    Dim x As Integer
    Dim Red As Long
    Dim Green As Long
    Dim Blue As Long
    Dim RedRange As Integer
    Dim GreenRange As Integer
    Dim BlueRange As Integer
    Dim CounterTwo As Long
    Dim IterationsTwo As Long

    CounterTwo = Counter
    IterationsTwo = Iterations
    
    For x = 2 To 100
        If Counter <= IterationRange(x) And Counter >= IterationRange(x - 1) Then
            RedRange = UserColorsRed(x) - UserColorsRed(x - 1)
            GreenRange = UserColorsGreen(x) - UserColorsGreen(x - 1)
            BlueRange = UserColorsBlue(x) - UserColorsBlue(x - 1)
        End If
    Next x
    
    Red = RedRange - ((CounterTwo + 1) / IterationsTwo * RedRange)
    Green = GreenRange - ((CounterTwo + 1) / IterationsTwo * GreenRange)
    Blue = BlueRange - ((CounterTwo + 1) / IterationsTwo * BlueRange)
    If Red < 0 Then Red = Red * -1
    If Green < 0 Then Green = Green * -1
    If Blue < 0 Then Blue = Blue * -1
    temp = RGB(Red, Green, Blue)

    AssignColor = temp
End Function
Sub DefaultColors()
    Dim i As Integer
    
    IterationRange(1) = 0
    IterationRange(2) = 8
    IterationRange(3) = 12
    IterationRange(4) = 18
    IterationRange(5) = 26
    IterationRange(6) = 128
    IterationRange(7) = 256
    IterationRange(8) = 1024

    For i = 9 To 100
        IterationRange(i) = 1000 + i
    Next i

    UserColorsRed(1) = 0
    UserColorsGreen(1) = 0
    UserColorsBlue(1) = 0

    UserColorsRed(2) = 0
    UserColorsGreen(2) = 255
    UserColorsBlue(2) = 0

    UserColorsRed(3) = 255
    UserColorsGreen(3) = -128
    UserColorsBlue(3) = 0

    UserColorsRed(4) = 240
    UserColorsGreen(4) = -128
    UserColorsBlue(4) = 16
    
    UserColorsRed(5) = 0
    UserColorsGreen(5) = 0
    UserColorsBlue(5) = 0
    
    UserColorsRed(6) = 0
    UserColorsGreen(6) = 128
    UserColorsBlue(6) = 0
    
    UserColorsRed(7) = 0
    UserColorsGreen(7) = 128
    UserColorsBlue(7) = 255
    
    UserColorsRed(8) = 255
    UserColorsGreen(8) = 0
    UserColorsBlue(8) = 255
    
    For i = 9 To 100
        UserColorsRed(5) = 0
        UserColorsGreen(5) = 0
        UserColorsBlue(5) = 0
    Next i
End Sub

Sub MandelbrotAndSave(par1 As Double, par2 As Double, par3 As Double, par4 As Double)
    XMAX = par1
    XMIN = par2
    YMAX = par3
    YMIN = par4
    
    Dim IX As Double
    Dim IY As Double
    Dim QX As Double
    Dim QY As Double
    Dim x As Double
    Dim Y As Double
    Dim temp As Double
    Dim Count As Integer
    Dim TheColor As Long
    Dim TempG As Long
    Dim TempSS As String * 1
    Dim TempColor As Long


    Form4.Show
    Form4.shape2.Width = 0

    For IY = Height_of_Pic To 1 Step -1
        For IX = 1 To Width_of_Pic
            x = (IX / Width_of_Pic) * (XMAX - XMIN) + XMIN
            Y = (IY / Height_of_Pic) * (YMAX - YMIN) + YMIN
        
            Count = 0
            QX = x
            QY = Y
        
            Do While Count < Iterations And (QX * QX + QY * QY) < 4
                Count = Count + 1
                temp = 2 * QX * QY + Y
                QX = QX * QX - QY * QY + x
                QY = temp
            Loop
        
            TheColor = AssignColor(Count)
'            Debug.Print TheColor
            
            TempColor = TheColor
            TempG = (2 ^ 8 - 1) * (2 ^ 8 - 1)
            'red
            TempColor = TempColor \ TempG
            TempSS = Chr(TempColor)
            Put #1, , TempSS
'            Debug.Print TempSS
'            Debug.Print TempColor
            'Green
            TempColor = TheColor
            TempColor = (TempColor Mod TempG) \ 255
            TempSS = Chr(TempColor)
            Put #1, , TempSS
'            Debug.Print TempColor
'            Debug.Print TempSS
            'blue
            TempColor = TheColor
            TempColor = (TempColor Mod TempG) Mod 255
            TempSS = Chr(TempColor)
            Put #1, , TempSS
'            Debug.Print TempColor
'            Debug.Print TempSS
        
        Next IX

    Form4.shape2.Width = (1 - (IY / Height_of_Pic)) * Form4.shape1.Width
    Next IY

    Form4.Hide
    Form4.shape2.Width = 0

End Sub
Sub Mandelbrot(par1 As Double, par2 As Double, par3 As Double, par4 As Double)
    Dim j As Integer
    Dim i As Integer
    
    If AddToHistory = 1 Then
        CurrentHistory = CurrentHistory + 1
        History(CurrentHistory).MaximumX = par1
        History(CurrentHistory).MinimumX = par2
        History(CurrentHistory).MaximumY = par3
        History(CurrentHistory).MinimumY = par4
        MiniMandelbrot par1, par2, par3, par4
        For j = 0 To 79
            For i = 0 To 59
                History(CurrentHistory).Preview(j, i) = TempBook.Preview(j, i)
            Next i
        Next j
    End If

    XMAX = par1
    XMIN = par2
    YMAX = par3
    YMIN = par4
    
    Dim IX As Double
    Dim IY As Double
    Dim QX As Double
    Dim QY As Double
    Dim x As Double
    Dim Y As Double
    Dim temp As Double
    Dim Count As Integer
    Dim TheColor As Long

    ReDim Pic(Width_of_Pic, Height_of_Pic)

    Form4.Show
    Form4.shape2.Width = 0

    For IY = 0 To Height_of_Pic
        For IX = 0 To Width_of_Pic
            x = (IX / Width_of_Pic) * (XMAX - XMIN) + XMIN
            Y = (IY / Height_of_Pic) * (YMAX - YMIN) + YMIN
        
            Count = 0
            QX = x
            QY = Y
        
            Do While Count < Iterations And (QX * QX + QY * QY) < 4
                Count = Count + 1
                temp = 2 * QX * QY + Y
                QX = QX * QX - QY * QY + x
                QY = temp
            Loop
        
            TheColor = AssignColor(Count)

            Pic(IX, IY) = TheColor
        
        Next IX

    Form4.shape2.Width = (IY / Height_of_Pic) * Form4.shape1.Width
    Next IY

    Form4.Hide
    Form4.shape2.Width = 0

End Sub


Sub MiniMandelbrot(par1 As Double, par2 As Double, par3 As Double, par4 As Double)

    XMAX = par1
    XMIN = par2
    YMAX = par3
    YMIN = par4

    Dim IX As Double
    Dim IY As Double
    Dim QX As Double
    Dim QY As Double
    Dim x As Double
    Dim Y As Double
    Dim temp As Double
    Dim Count As Integer
    Dim TheColor As Long

    For IY = 0 To 59
        For IX = 0 To 79
            x = (IX / 79) * (XMAX - XMIN) + XMIN
            Y = (IY / 59) * (YMAX - YMIN) + YMIN
            Count = 0
            QX = x
            QY = Y
        
            Do While Count < 16 And (QX * QX + QY * QY) < 4
                Count = Count + 1
                temp = 2 * QX * QY + Y
                QX = QX * QX - QY * QY + x
                QY = temp
            Loop
        
            TheColor = AssignColor(Count)

            TempBook.Preview(IX, IY) = TheColor
        
        Next IX
    Next IY

End Sub

Sub OpenBookmark()
    Dim x As Integer
    
    On Error GoTo ErrorHandler
    Open "bookmark.dat" For Random Access Read As #1 Len = 22000

    CurrentData = 1

    Do While Not EOF(1)
        Get #1, , Data(CurrentData)
        CurrentData = CurrentData + 1
    Loop
    CurrentData = CurrentData - 1
    
    Close #1
ErrorHandler:
End Sub
Sub ReDraw()
    Dim x As Integer
    Dim Y As Integer
    For Y = 0 To Height_of_Pic
        For x = 0 To Width_of_Pic
            Form1.PSet (x, Y), Pic(x, Y)
        Next x
    Next Y

End Sub

Sub SaveBookmark()
    Dim x As Integer
    On Error GoTo ErrorHandlerTwo
    
    Kill "bookmark.dat"
    Open "bookmark.dat" For Random Access Write As #1 Len = 22000

    For x = 1 To MaximumBookmarks
        Put #1, x, Data(x)
    Next x
    Close #1
    
    
    
ErrorHandlerTwo:
End Sub



