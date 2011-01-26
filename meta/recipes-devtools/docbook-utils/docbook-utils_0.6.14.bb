SUMMARY = "Utilities for formatting and manipulating DocBook documents"
DESCRIPTION = "A collection of all the free software tools you need to \
work on and format DocBook documents."
HOMEPAGE = "http://sources.redhat.com/docbook-tools/"
SECTION = "console/utils"
PRIORITY = "required"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"
DEPENDS = "openjade-native sgmlspl-native docbook-dsssl-stylesheets-native docbook-sgml-dtd-3.1-native"

PR = "r0"

SRC_URI = "ftp://sources.redhat.com/pub/docbook-tools/new-trials/SOURCES/docbook-utils-${PV}.tar.gz"

SRC_URI[md5sum] = "6b41b18c365c01f225bc417cf632d81c"
SRC_URI[sha256sum] = "48faab8ee8a7605c9342fb7b906e0815e3cee84a489182af38e8f7c0df2e92e9"

inherit autotools

BBCLASSEXTEND = "native"
