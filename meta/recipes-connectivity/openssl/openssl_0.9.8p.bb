require openssl.inc

PR = "r3"
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
            file://debian/rc4-amd64.patch \
            file://debian/rehash-crt.patch \
            file://debian/rehash_pod.patch \
            file://debian/shared-lib-ext.patch \
            file://debian/stddef.patch \
            file://debian/version-script.patch \
            file://debian/perl-path.diff"

SRC_URI[md5sum] = "7f24047f70364c9eabc94899e356ce39"
SRC_URI[sha256sum] = "b2645e2a2af221fa230b5ef6aa2b9388a875801b74cbddbb16be557f80f45242"

SRC_URI += "file://configure-targets.patch \
            file://shared-libs.patch"

BBCLASSEXTEND = "native nativesdk"
