require e2fsprogs.inc

PR = "r20"

SRC_URI += "file://quotefix.patch;patch=1 \
            file://acinclude.m4"

SRC_URI[md5sum] = "1b24a21fc0c2381ef420961cbfec733f"
SRC_URI[sha256sum] = "9c26d0dc20bfdafd0f27f3564791d6121d1c1876549d4f2ff1e41955c9bb9f20"
#file://no-hardlinks.patch;patch=1

PARALLEL_MAKE = ""

EXTRA_OECONF += " --sbindir=${base_sbindir} --enable-elf-shlibs --disable-libuuid"
EXTRA_OECONF_darwin = "--sbindir=${base_sbindir} --enable-bsd-shlibs"
EXTRA_OECONF_darwin8 = "--sbindir=${base_sbindir} --enable-bsd-shlibs"

do_configure_prepend () {
	cp ${WORKDIR}/acinclude.m4 ${S}/
}

do_compile_prepend () {
	find ./ -print | grep -v ./patches | xargs chmod u=rwX
	( cd util; ${BUILD_CC} subst.c -o subst )
}

do_install () {
	oe_runmake 'DESTDIR=${D}' install
	oe_runmake 'DESTDIR=${D}' install-libs
	# We use blkid from util-linux now so remove from here
	rm -f ${D}${libdir}/libblkid*
	rm -rf ${D}${includedir}/blkid
	rm -f ${D}${libdir}/pkgconfig/blkid.pc
}

# blkid used to be part of e2fsprogs but is useful outside, add it
# as an RDEPENDS_${PN} so that anything relying on it being in e2fsprogs
# still works
RDEPENDS_e2fsprogs = "e2fsprogs-blkid e2fsprogs-badblocks"

PACKAGES =+ "e2fsprogs-blkid e2fsprogs-e2fsck e2fsprogs-mke2fs e2fsprogs-fsck e2fsprogs-tune2fs e2fsprogs-badblocks"
FILES_e2fsprogs-blkid = "${base_sbindir}/blkid"
FILES_e2fsprogs-fsck = "${base_sbindir}/fsck"
FILES_e2fsprogs-e2fsck = "${base_sbindir}/e2fsck ${base_sbindir}/fsck.ext*"
FILES_e2fsprogs-mke2fs = "${base_sbindir}/mke2fs ${base_sbindir}/mkfs.ext*"
FILES_e2fsprogs-tune2fs = "${base_sbindir}/tune2fs ${base_sbindir}/e2label ${base_sbindir}/findfs"
FILES_e2fsprogs-badblocks = "${base_sbindir}/badblocks"

BBCLASSEXTEND = "native"
