require e2fsprogs.inc

PR = "r14"

SRC_URI += "file://no-hardlinks.patch;patch=1"

PARALLEL_MAKE = ""

EXTRA_OECONF += " --sbindir=${base_sbindir} --enable-elf-shlibs"
EXTRA_OECONF_darwin = "--enable-dynamic-e2fsck --sbindir=${base_sbindir} --enable-bsd-shlibs"
EXTRA_OECONF_darwin8 = "--enable-dynamic-e2fsck --sbindir=${base_sbindir} --enable-bsd-shlibs"

do_compile_prepend () {
	find ./ -print|xargs chmod u=rwX
	( cd util; ${BUILD_CC} subst.c -o subst )
}

do_install () {
	oe_runmake 'DESTDIR=${D}' install
	oe_runmake 'DESTDIR=${D}' install-libs
	# We use blkid from util-linux now so remove from here
	rm -f ${D}${libdir}/libblkid*
	rm -rf ${D}${includedir}/blkid
	rm -f ${D}${libdir}/pkgconfig/blkid.oc
}

# blkid used to be part of e2fsprogs but is useful outside, add it
# as an RDEPENDS so that anything relying on it being in e2fsprogs
# still works
RDEPENDS_e2fsprogs = "e2fsprogs-blkid e2fsprogs-uuidgen e2fsprogs-badblocks"

PACKAGES =+ "e2fsprogs-blkid e2fsprogs-uuidgen e2fsprogs-e2fsck e2fsprogs-mke2fs e2fsprogs-fsck e2fsprogs-tune2fs e2fsprogs-badblocks libuuid"
FILES_e2fsprogs-blkid = "${base_sbindir}/blkid"
FILES_e2fsprogs-uuidgen = "${bindir}/uuidgen"
FILES_e2fsprogs-fsck = "${base_sbindir}/fsck"
FILES_e2fsprogs-e2fsck = "${base_sbindir}/e2fsck ${base_sbindir}/fsck.ext*"
FILES_e2fsprogs-mke2fs = "${base_sbindir}/mke2fs ${base_sbindir}/mkfs.ext*"
FILES_e2fsprogs-tune2fs = "${base_sbindir}/tune2fs ${base_sbindir}/e2label ${base_sbindir}/findfs"
FILES_e2fsprogs-badblocks = "${base_sbindir}/badblocks"
FILES_libuuid = "${libdir}/libuuid.so.*"

BBCLASSEXTEND = "native"
