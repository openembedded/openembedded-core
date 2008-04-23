DESCRIPTION = "Target packages for the standalone SDK"
PR = "r3"
LICENSE = "MIT"
ALLOW_EMPTY = "1"

PACKAGES = "${PN} ${PN}-dbg"

RDEPENDS_${PN} = "\
    libgcc \
    libstdc++ \
    "

GLIBC_DEPENDENCIES = "\
    libsegfault \
    glibc \
    glibc-dbg \
    glibc-dev \
    glibc-utils \
    glibc-thread-db \
    glibc-localedata-i18n \
    glibc-gconv-ibm850 \
    glibc-gconv-cp1252 \
    glibc-gconv-iso8859-1 \
    glibc-gconv-iso8859-15 \
    locale-base-en-gb \
    "

RDEPENDS_${PN}_append_linux = "${GLIBC_DEPENDENCIES}"
RDEPENDS_${PN}_append_linux-gnueabi = "${GLIBC_DEPENDENCIES}"

UCLIBC_DEPENDENCIES = "\
    uclibc \
    uclibc-dbg \
    uclibc-dev \
    uclibc-utils \
    uclibc-thread-db \
    "

RDEPENDS_${PN}_append_linux-uclibc = "${UCLIBC_DEPENDENCIES}"
RDEPENDS_${PN}_append_linux-uclibcgnueabi = "${UCLIBC_DEPENDENCIES}"

