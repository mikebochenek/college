
Private Sub Command1_Click()
temp = "NAME: ______________________   DATE: ___________________   SCORE: ______" & Chr(13) & Chr(13) & Chr(13)
For jj = 1 To 10
    Randomize
    temp = temp & Int(Rnd * 9 + 1) & " + " & Int(Rnd * 9 + 1) & " = ___        "
    temp = temp & Int(Rnd * 9 + 1) & " + " & Int(Rnd * 9 + 1) & " = ___        "
    temp = temp & Int(Rnd * 9 + 1) & " + " & Int(Rnd * 9 + 1) & " = ___        "
    temp = temp & Int(Rnd * 9 + 1) & " + " & Int(Rnd * 9 + 1) & " = ___        "
    temp = temp & Int(Rnd * 9 + 1) & " + " & Int(Rnd * 9 + 1) & " = ___"
    temp = temp & Chr(13) & Chr(13)
Next jj
frmprinter.Show
frmprinter.Label1 = temp
frmprinter.PrintForm
Unload frmprinter
Unload frmConfirm
End Sub


Private Sub Command2_Click()
Unload frmConfirm

End Sub


