DESCRIPTION = "Target packages for the standalone SDK"
PR = "r3"
LICENSE = "MIT"
ALLOW_EMPTY = "1"

PACKAGES = "${PN} ${PN}-dbg"

RDEPENDS_${PN} = "\
    glibc \
    glibc-dbg \
    glibc-dev \
    glibc-utils \
    libsegfault \
    glibc-thread-db \
    glibc-localedata-i18n \
    glibc-gconv-ibm850 \
    glibc-gconv-cp1252 \
    glibc-gconv-iso8859-1 \
    glibc-gconv-iso8859-15 \
    locale-base-en-gb \
    libgcc \
    libstdc++ \
    "
