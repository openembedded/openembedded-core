require e2fsprogs.inc


SRC_URI += "file://acinclude.m4 \
            file://remove.ldconfig.call.patch \
            file://debugfs-too-short.patch \
            file://debugfs-sparse-copy.patch \
            file://fix-icache.patch \
            file://debugfs-extent-header.patch \
            file://populate-extfs.sh \
"

SRC_URI[md5sum] = "8ef664b6eb698aa6b733df59b17b9ed4"
SRC_URI[sha256sum] = "b984aaf1fe888d6a4cf8c2e8d397207879599b5368f1d33232c1ec9d68d00c97"

EXTRA_OECONF += "--libdir=${base_libdir} --sbindir=${base_sbindir} --enable-elf-shlibs --disable-libuuid --disable-uuidd"
EXTRA_OECONF_darwin = "--libdir=${base_libdir} --sbindir=${base_sbindir} --enable-bsd-shlibs"

do_configure_prepend () {
	cp ${WORKDIR}/acinclude.m4 ${S}/
}

do_compile_prepend () {
	find ./ -print | grep -v ./patches | xargs chmod u=rwX
	( cd ${S}/util; ${BUILD_CC} subst.c -o ${B}/util/subst )
}

do_install () {
	oe_runmake 'DESTDIR=${D}' install
	oe_runmake 'DESTDIR=${D}' install-libs
	# We use blkid from util-linux now so remove from here
	rm -f ${D}${base_libdir}/libblkid*
	rm -rf ${D}${includedir}/blkid
	rm -f ${D}${base_libdir}/pkgconfig/blkid.pc
	rm -f ${D}${base_sbindir}/blkid
	rm -f ${D}${base_sbindir}/fsck
	rm -f ${D}${base_sbindir}/findfs
}

do_install_append () {
	# e2initrd_helper and the pkgconfig files belong in libdir
	if [ ! ${D}${libdir} -ef ${D}${base_libdir} ]; then
		install -d ${D}${libdir}
		mv ${D}${base_libdir}/e2initrd_helper ${D}${libdir}
		mv ${D}${base_libdir}/pkgconfig ${D}${libdir}
	fi
	install -m 0755 ${WORKDIR}/populate-extfs.sh ${D}${bindir}
}

RDEPENDS_e2fsprogs = "e2fsprogs-badblocks"

PACKAGES =+ "e2fsprogs-e2fsck e2fsprogs-mke2fs e2fsprogs-tune2fs e2fsprogs-badblocks"
PACKAGES =+ "libcomerr libss libe2p libext2fs"

FILES_e2fsprogs-e2fsck = "${base_sbindir}/e2fsck ${base_sbindir}/fsck.ext*"
FILES_e2fsprogs-mke2fs = "${base_sbindir}/mke2fs ${base_sbindir}/mkfs.ext* ${sysconfdir}/mke2fs.conf"
FILES_e2fsprogs-tune2fs = "${base_sbindir}/tune2fs ${base_sbindir}/e2label"
FILES_e2fsprogs-badblocks = "${base_sbindir}/badblocks"
FILES_libcomerr = "${base_libdir}/libcom_err.so.*"
FILES_libss = "${base_libdir}/libss.so.*"
FILES_libe2p = "${base_libdir}/libe2p.so.*"
FILES_libext2fs = "${libdir}/e2initrd_helper ${base_libdir}/libext2fs.so.*"
FILES_${PN}-dev += "${datadir}/*/*.awk ${datadir}/*/*.sed ${base_libdir}/*.so"

BBCLASSEXTEND = "native"
