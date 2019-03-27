SUMMARY = "Tool for creating HTML, PDF, EPUB, man pages"
DESCRIPTION = "AsciiDoc is a text document format for writing short documents, \
articles, books and UNIX man pages."

HOMEPAGE = "http://asciidoc.org/"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=8ca43cbc842c2336e835926c2166c28b \
                    file://COPYRIGHT;md5=029ad5428ba5efa20176b396222d4069"

SRC_URI = "git://github.com/asciidoc/asciidoc-py3;protocol=https"
SRCREV = "618f6e6f6b558ed1e5f2588cd60a5a6b4f881ca0"
PV .= "+py3-git${SRCPV}"

DEPENDS = "libxml2-native libxslt-native docbook-xml-dtd4-native"

S = "${WORKDIR}/git"

# Not using automake
inherit autotools-brokensep
CLEANBROKEN = "1"

# target and nativesdk needs python3, but for native we can use the host.
RDEPENDS_${PN} += "python3"
RDEPENDS_remove_class-native = "python3"

BBCLASSEXTEND = "native nativesdk"
