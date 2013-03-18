require e2fsprogs.inc

PR = "r0"

SRC_URI += "file://acinclude.m4 \
            file://remove.ldconfig.call.patch \
"

SRC_URI[md5sum] = "a1ec22ef003688dae9f76c74881b22b9"
SRC_URI[sha256sum] = "dc6501b2e75d205e425196d753d92b129c568525d8aad08085c0aa69ee9e7345"

EXTRA_OECONF += "--libdir=${base_libdir} --sbindir=${base_sbindir} --enable-elf-shlibs --disable-libuuid --disable-uuidd"
EXTRA_OECONF_darwin = "--libdir=${base_libdir} --sbindir=${base_sbindir} --enable-bsd-shlibs"
EXTRA_OECONF_darwin8 = "--libdir=${base_libdir} --sbindir=${base_sbindir} --enable-bsd-shlibs"

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
