SUMMARY = "A multi-platform support library with a focus on asynchronous I/O"
HOMEPAGE = "https://github.com/libuv/libuv"
DESCRIPTION = "libuv is a multi-platform support library with a focus on asynchronous I/O. It was primarily developed for use by Node.js, but it's also used by Luvit, Julia, pyuv, and others."
BUGTRACKER = "https://github.com/libuv/libuv/issues"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=74b6f2f7818a4e3a80d03556f71b129b \
                    file://LICENSE-extra;md5=f9307417749e19bd1d6d68a394b49324"

SRCREV = "d2e56a5e8d3e39947b78405ca6e4727c70f5568a"
SRC_URI = "git://github.com/libuv/libuv.git;branch=v1.x;protocol=https"
UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\d+(\.\d+)+)"

S = "${WORKDIR}/git"

inherit autotools

do_configure() {
    ${S}/autogen.sh || bbnote "${PN} failed to autogen.sh"
    oe_runconf
}

BBCLASSEXTEND = "native"
