SUMMARY = "Python parsing module"
DESCRIPTION = "The pyparsing module is an alternative approach to creating \
and executing simple grammars, vs. the traditional lex/yacc approach, or \
the use of regular expressions. The pyparsing module provides a library of \
classes that client code uses to construct the grammar directly in Python \
code."
HOMEPAGE = "https://github.com/pyparsing/pyparsing/"
BUGTRACKER = "https://github.com/pyparsing/pyparsing/issues"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9c8a9625037eff2407f8456010875183"

SRC_URI[sha256sum] = "47fad0f17ac1e2cad3de3b458570fbc9b03560aa029ed5e16ee5554da9a2251c"

inherit pypi python_flit_core

RDEPENDS:${PN} += " \
    python3-datetime \
    python3-debugger \
    python3-html \
    python3-json \
    python3-netclient \
    python3-pprint \
    python3-stringold \
    python3-threading \
"

BBCLASSEXTEND = "native nativesdk"
