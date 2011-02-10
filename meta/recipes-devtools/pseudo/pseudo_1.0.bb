require pseudo.inc

PR = "r0"

SRC_URI = "http://www.yoctoproject.org/downloads/${BPN}/${BPN}-${PV}.tar.bz2 \
           file://static_sqlite.patch"

SRC_URI[md5sum] = "a329bd00ba78711bcacd48fc1982af6a"
SRC_URI[sha256sum] = "cb41c97094044144ef4027ab9dbba009f3cb1c23e6bec9640f29d530524ad60f"
