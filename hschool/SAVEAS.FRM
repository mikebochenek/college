Private Sub Command1_Click()
    Open Label1 For Binary Access Write As #1
    Dim TempZero As String * 1
    Dim TempS As String * 1
    Dim XX As Integer
    Dim YY As Integer
    Dim TempF As Long
    Dim TempG As Long
    Dim FinalString As String * 17
    Dim TempHeightOfPic As Integer
    Dim TempWidthOfPic As Integer
        
    FinalString = "TRUEVISION-TARGA."
    TempG = (2 ^ 8 - 1) * (2 ^ 8 - 1)
    
    'General
    TempZero = Chr(0)
    Debug.Print TempZero
    Put #1, , TempZero
    Put #1, , TempZero
    TempS = Chr(2)
    Put #1, , TempS
    'Color Map
    Put #1, , TempZero
    Put #1, , TempZero
    Put #1, , TempZero
    Put #1, , TempZero
    Put #1, , TempZero
    'Image
    Put #1, , TempZero
    Put #1, , TempZero
    Put #1, , TempZero
    Put #1, , TempZero
    TempS = Chr(Val(Text2) Mod 256)
    Put #1, , TempS
    TempS = Chr(Val(Text2) \ 256)
    Put #1, , TempS
    TempS = Chr(Val(Text3) Mod 256)
    Put #1, , TempS
    TempS = Chr(Val(Text3) \ 256)
    Put #1, , TempS
    TempS = Chr(24)
    Put #1, , TempS
    Put #1, , TempZero
    
    Form4.Show
    Form4.shape2.Width = 0
    
    'Actual pixels
    If Val(Text3) <> Height_of_Pic Or Val(Text2) <> Width_of_Pic Then
        TempHeightOfPic = Height_of_Pic
        TempWidthOfPic = Width_of_Pic
        Height_of_Pic = Val(Text3)
        Width_of_Pic = Val(Text2)
        MandelbrotAndSave ZoomXMax, ZoomXMin, ZoomYMax, ZoomYMin
    Else
        TempHeightOfPic = Height_of_Pic
        TempWidthOfPic = Width_of_Pic
        For YY = Height_of_Pic To 1 Step -1
            For XX = 1 To Width_of_Pic
                'red
                TempF = Pic(XX, YY)
                TempF = TempF \ TempG
                TempS = Chr(TempF)
                Put #1, , TempS
                'green
                TempF = Pic(XX, YY)
                TempF = (TempF Mod TempG) \ 255
                TempS = Chr(TempF)
                Put #1, , TempS
                'blue
                TempF = Pic(XX, YY)
                TempF = (TempF Mod TempG) Mod 255
                TempS = Chr(TempF)
                Put #1, , TempS
            Next XX
        Form4.shape2.Width = (1 - (YY / Height_of_Pic)) * Form4.shape1.Width
        Next YY
    End If
    Form4.Hide
    Form4.shape2.Width = 0
    Width_of_Pic = TempWidthOfPic
    Height_of_Pic = TempHeightOfPic
    
'    Put #1, , TempZero
'    Put #1, , TempZero
'    Put #1, , TempZero
'    TempS = &H6
'    Put #1, , TempS
'    Put #1, , TempZero
'    Put #1, , TempZero
'    Put #1, , TempZero
'    Put #1, , TempZero
'    Put #1, , FinalString
'    Put #1, , TempZero
    
    Close #1

    Form7.Hide
    Form1.Show
    ReDraw


End Sub

Private Sub Command2_Click()
    Form7.Hide
    Form1.Show
    ReDraw
    End Sub

Private Sub Dir1_Change()
    File1.Path = Dir1.Path
    Label1 = Dir1.Path
    Text1_Change
End Sub

Private Sub Drive1_Change()
    Dir1.Path = Drive1.Drive
    Label1 = Dir1.Path
    Text1_Change
End Sub


Private Sub File1_Click()
    Dim tempstring As String
    Label1 = Dir1.Path
    tempstring = Label1

    Label1 = Dir1.Path & "\" & File1.filename

    If Len(tempstring) = 3 Then
        Label1 = Dir1.Path & File1.filename
    End If

    Text1 = File1.filename
End Sub

Private Sub Text1_Change()
    Dim tempstring
    Label1 = Dir1.Path
    tempstring = Label1

    Label1 = Dir1.Path & "\" & Text1

    If Len(tempstring) = 3 Then
        Label1 = Dir1.Path & Text1
    End If

End Sub


