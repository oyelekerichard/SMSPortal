<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>${provider} Account Creation</title>
  </head>
  <body bgcolor="#eeeeee">

    <table align="center" cellpadding="0" width="400" cellspacing="0">
      <tr>
        <td width="100%" align="center" valign="top">

          <p>&nbsp;</p>
          <p>&nbsp;</p>
          <div style="width:600px; overflow: hidden; box-shadow: 0 3px 3px #b0bcbf; border-radius: 10pt; -moz-border-radius: 10pt; -webkit-border-radius: 10pt; background-color: #ffffff;">
            <table cellpadding="0" cellspacing="0" width="100%">
              <tr>
                <td width="100%" height="41" align="left" style="background-color: #00aff0">
                  <table>
                    <tr>
                      <td width="450"></td>
                      <td><img id="image" src="cid:image" alt="logo" width="100" style="position: absolute; box-shadow: 0 3px 3px #b0bcbf; border: 7px solid #ffffff" /></td>
                    </tr>
                  </table>

                </td>
              </tr>
              <tr>
                <td align="left" style="font-size: 11px; color: #333; font-family: Tahoma; padding: 13pt">
                  <p style="font-size: 14pt; font-weight: bold">Dear ${fullName},</p>
                  <br>
                    <p style="font-size: 12px;">An account was created for you on CrownInteractive SMS Portal on ${dateOfTransaction}.<br/>
You can now see SMS transactions as they hit the system.</p>
                    <br/>
                    <p style="font-size: 12px;">Your Username is <b>${userEmail}</b></p>
                    <p style="font-size: 12px;">Your Password is <b>${password}</b>. </p>
                    <br/>
                    <p style="font-size: 12px;">Please <a href="${link}">click here</a> to login to the SMS portal using the details above..</p>
                    <br/>
                    <p>Regards,</br>
                        <b>CrownInteractive SMS Team</b></p>

                </td>
              </tr>
            </table>
          </div>


        </td>
      </tr>
    </table>

  </body>

</html>