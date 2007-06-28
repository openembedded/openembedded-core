DESCRIPTION = "Target packages for the standalone SDK (external toolchain)"
PR = "r0"
LICENSE = "MIT"
ALLOW_EMPTY = "1"

PACKAGES = "${PN}"

RDEPENDS_${PN} = "\
    glibc \
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
