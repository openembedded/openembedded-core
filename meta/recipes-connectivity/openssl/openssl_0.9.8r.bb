require openssl.inc

PR = "r7"
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

SRC_URI[md5sum] = "0352932ea863bc02b056cda7c9ac5b79"
SRC_URI[sha256sum] = "42b2368f786b05ed3be846838dce126b4e8e3dba8fb2e0ce83102df28c102fad"

SRC_URI += "file://configure-targets.patch \
            file://shared-libs.patch"

BBCLASSEXTEND = "native nativesdk"
