require ncurses.inc

REVISION = "20150329"

PR = "${INC_PR}.1"

SRC_URI += "file://tic-hang.patch \
            file://config.cache \
"
# commit id corresponds to the above listed REVISION
SRCREV = "6286e14dac28811dbfd325b8d4c23f0d4e37eaf0"
S = "${WORKDIR}/git"
