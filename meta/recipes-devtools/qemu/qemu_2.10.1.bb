require qemu.inc

inherit ptest

RDEPENDS_${PN}-ptest = "bash make"

LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
                    file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913"

SRC_URI = "http://wiki.qemu-project.org/download/${BP}.tar.bz2 \
           file://powerpc_rom.bin \
           file://disable-grabs.patch \
           file://exclude-some-arm-EABI-obsolete-syscalls.patch \
           file://wacom.patch \
           file://add-ptest-in-makefile-v10.patch \
           file://run-ptest \
           file://qemu-enlarge-env-entry-size.patch \
           file://no-valgrind.patch \
           file://pathlimit.patch \
           file://qemu-2.5.0-cflags.patch \
           file://glibc-2.25.patch \
           file://0001-tpm-Clean-up-driver-registration-lookup.patch \
           file://0002-tpm-Clean-up-model-registration-lookup.patch \
           file://0003-tpm-backend-Remove-unneeded-member-variable-from-bac.patch \
           file://0004-tpm-backend-Move-thread-handling-inside-TPMBackend.patch \
           file://0005-tpm-backend-Initialize-and-free-data-members-in-it-s.patch \
           file://0006-tpm-backend-Made-few-interface-methods-optional.patch \
           file://0007-tpm-backend-Add-new-api-to-read-backend-TpmInfo.patch \
           file://0008-tpm-backend-Move-realloc_buffer-implementation-to-tp.patch \
           file://0009-tpm-passthrough-move-reusable-code-to-utils.patch \
           file://0010-tpm-Added-support-for-TPM-emulator.patch \
           file://0011-tpm-Move-tpm_cleanup-to-right-place.patch \
           file://0012-tpm-Use-EMSGSIZE-instead-of-EBADMSG-to-compile-on-Op.patch \
           file://chardev-connect-socket-to-a-spawned-command.patch \
           file://apic-fixup-fallthrough-to-PIC.patch \
           file://ppc_locking.patch \
           "
UPSTREAM_CHECK_REGEX = "qemu-(?P<pver>\d+\..*)\.tar"


SRC_URI_append_class-native = " \
            file://fix-libcap-header-issue-on-some-distro.patch \
            file://cpus.c-qemu_cpu_kick_thread_debugging.patch \
            "

SRC_URI[md5sum] = "b375373f688bea0cd8865b966dad15e3"
SRC_URI[sha256sum] = "8e040bc7556401ebb3a347a8f7878e9d4028cf71b2744b1a1699f4e741966ba8"

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

