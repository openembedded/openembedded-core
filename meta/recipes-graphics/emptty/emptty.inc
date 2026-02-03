SUMMARY = "Dead simple CLI Display Manager on TTY"
DESCRIPTION = "Emptty is a simple display manager with a command line greeter \
It supports both X11 and Wayland sessions, exporting required variables and \
creating user paths as necessary."

HOMEPAGE = "https://github.com/tvrzna/emptty"
BUGTRACKER = "https://github.com/tvrzna/emptty/issues"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://src/${GO_IMPORT}/LICENSE;md5=d1e4d12c7d1d17367ba5668706a405ba"

S = "${UNPACKDIR}"

FILESEXTRAPATHS:append = ":${THISDIR}/emptty"

GO_IMPORT = "github.com/tvrzna/emptty"
GO_IMPORT_pam = "github.com/msteinert/pam"
SRC_URI = "\
    git://${GO_IMPORT}.git;protocol=https;branch=master;destsuffix=src/${GO_IMPORT} \
    git://${GO_IMPORT_pam}.git;protocol=https;branch=master;name=pam;destsuffix=src/${GO_IMPORT_pam} \
    file://pamconf \
    "
SRCREV = "bb203ad0a224731a7a2ff50deeb342ab79c4af09"
SRCREV_pam = "50ded1b0e7864b9bf75005eb945a8ec826bcf69d"

SRCREV_FORMAT .= "_pam"
