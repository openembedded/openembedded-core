DESCRIPTION = "SDK packages"
PR = "r12"
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
    glibc-gconv-iso8859-1 \
    glibc-localedata-i18n \
    locale-base-en-gb \
    libgcc \
    libstdc++ \
    "
