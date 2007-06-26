DESCRIPTION = "SDK packages"
PR = "r16"
LICENSE = "MIT"
ALLOW_EMPTY = "1"

PACKAGES = "\
    task-sdk-bare \
    "

RDEPENDS_task-sdk-bare = "\
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

RRECOMMENDS_task-sdk-bare = "\
    glibc-binary-localedata-en-gb \
    "