AC_PREREQ(2.53)
AC_INIT(poky-doc-tools, 0.1, http://o-hand.com)
AM_INIT_AUTOMAKE()

AC_PATH_PROG(HAVE_XSLTPROC, xsltproc, no)
if test x$HAVE_XSLTPROC = xno; then
  AC_MSG_ERROR([Required xsltproc program not found])
fi

AC_PATH_PROG(HAVE_FOP, fop, no)
if test x$HAVE_FOP = xno; then
  AC_MSG_ERROR([Required fop program not found])
fi

AC_CHECK_FILE([/usr/share/xml/docbook/stylesheet/nwalsh/template/titlepage.xsl],HAVE_NWALSH="yes", HAVE_NWALSH="no")
if test x$HAVE_FOP = xno; then
  AC_MSG_ERROR([Required 'nwalsh' docbook stylesheets not found])
fi

AC_OUTPUT([
Makefile
common/Makefile
])

echo "
                == poky-doc-tools $VERSION configured successfully. ==
"