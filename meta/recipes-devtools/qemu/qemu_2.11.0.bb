require qemu.inc

inherit ptest

RDEPENDS_${PN}-ptest = "bash make"

LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
                    file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913"

SRC_URI = "http://wiki.qemu-project.org/download/${BP}.tar.bz2 \
           file://powerpc_rom.bin \
           file://disable-grabs.patch \
           file://wacom.patch \
           file://add-ptest-in-makefile-v10.patch \
           file://run-ptest \
           file://qemu-enlarge-env-entry-size.patch \
           file://no-valgrind.patch \
           file://pathlimit.patch \
           file://qemu-2.5.0-cflags.patch \
           file://glibc-2.25.patch \
           file://chardev-connect-socket-to-a-spawned-command.patch \
           file://apic-fixup-fallthrough-to-PIC.patch \
           file://linux-user-Fix-webkitgtk-hangs-on-32-bit-x86-target.patch \
           "
UPSTREAM_CHECK_REGEX = "qemu-(?P<pver>\d+\..*)\.tar"


SRC_URI_append_class-native = " \
            file://fix-libcap-header-issue-on-some-distro.patch \
            file://cpus.c-qemu_cpu_kick_thread_debugging.patch \
            "

SRC_URI[md5sum] = "335994a755bc655e88a87aeb36bfc0b9"
SRC_URI[sha256sum] = "c4f034c7665a84a1c3be72c8da37f3c31ec063475699df062ab646d8b2e17fcb"

COMPATIBLE_HOST_mipsarchn32 = "null"
COMPATIBLE_HOST_mipsarchn64 = "null"

do_install_append() {
    # Prevent QA warnings about installed ${localstatedir}/run
    if [ -d ${D}${localstatedir}/run ]; then rmdir ${D}${localstatedir}/run; fi
    install -Dm 0755 ${WORKDIR}/powerpc_rom.bin ${D}${datadir}/qemu
}

do_compile_ptest() {
	make buildtest-TESTS
}

do_install_ptest() {
	cp -rL ${B}/tests ${D}${PTEST_PATH}
	find ${D}${PTEST_PATH}/tests -type f -name "*.[Sshcod]" | xargs -i rm -rf {}

	cp ${S}/tests/Makefile.include ${D}${PTEST_PATH}/tests
	# Don't check the file genreated by configure
	sed -i -e '/wildcard config-host.mak/d' \
	       -e '$ {/endif/d}' ${D}${PTEST_PATH}/tests/Makefile.include
}

