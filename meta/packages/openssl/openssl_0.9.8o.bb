require openssl.inc

PR = "r0"
SRC_URI += "file://debian/ca.patch \
            file://debian/config-hurd.patch;apply=no \
            file://debian/debian-targets.patch \
            file://debian/engines-path.patch \
            file://debian/kfreebsd-pipe.patch;apply=no \
            file://debian/make-targets.patch \
            file://debian/man-dir.patch \
            file://debian/man-section.patch \
            file://debian/no-rpath.patch \
            file://debian/no-symbolic.patch \
            file://debian/pic.patch \
            file://debian/pkg-config.patch \
            file://debian/valgrind.patch \
            file://debian/rc4-amd64.patch \
            file://debian/rehash-crt.patch \
            file://debian/rehash_pod.patch \
            file://debian/shared-lib-ext.patch \
            file://debian/stddef.patch \
            file://debian/version-script.patch \
            file://debian/perl-path.diff"

SRC_URI += "file://configure-targets.patch \
            file://shared-libs.patch"

BBCLASSEXTEND = "native"
