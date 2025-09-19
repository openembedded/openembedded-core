SUMMARY = "Python parsing module"
DESCRIPTION = "The pyparsing module is an alternative approach to creating \
and executing simple grammars, vs. the traditional lex/yacc approach, or \
the use of regular expressions. The pyparsing module provides a library of \
classes that client code uses to construct the grammar directly in Python \
code."
HOMEPAGE = "https://github.com/pyparsing/pyparsing/"
BUGTRACKER = "https://github.com/pyparsing/pyparsing/issues"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=657a566233888513e1f07ba13e2f47f1"

SRC_URI[sha256sum] = "fff89494f45559d0f2ce46613b419f632bbb6afbdaed49696d322bcf98a58e99"

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
