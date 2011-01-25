SECTION = "libs"
DEPENDS = "virtual/${TARGET_PREFIX}gcc-initial linux-libc-headers"
PROVIDES = "virtual/${TARGET_PREFIX}libc-initial"
FILESPATH = "${@base_set_filespath([ '${FILE_DIRNAME}/glibc-${PV}', '${FILE_DIRNAME}/glibc-2.4', '${FILE_DIRNAME}/glibc', '${FILE_DIRNAME}/files', '${FILE_DIRNAME}' ], d)}"

PACKAGES = ""
PACKAGES_DYNAMIC = ""

STAGINGCC = "gcc-cross-initial"
STAGINGCC_virtclass-nativesdk = "gcc-crosssdk-initial"

do_configure () {
	sed -ie 's,{ (exit 1); exit 1; }; },{ (exit 0); }; },g' ${S}/configure
	chmod +x ${S}/configure
	find ${S} -name "configure" | xargs touch
	${S}/configure --host=${TARGET_SYS} --build=${BUILD_SYS} \
		--without-cvs --disable-sanity-checks \
		--with-headers=${STAGING_DIR_TARGET}${includedir} \
		--enable-hacker-mode
	if grep -q GLIBC_2.3 ${S}/ChangeLog; then
		# glibc-2.3.x passes cross options to $(CC) when generating errlist-compat.c, which fails without a real cross-compiler.
		# Fortunately, we don't need errlist-compat.c, since we just need .h files,
		# so work around this by creating a fake errlist-compat.c and satisfying its dependencies.
		# Another workaround might be to tell configure to not use any cross options to $(CC).
		# The real fix would be to get install-headers to not generate errlist-compat.c.
		make sysdeps/gnu/errlist.c
		mkdir -p stdio-common
		touch stdio-common/errlist-compat.c
	fi
}

do_compile () {
	:
}

do_install () {
	oe_runmake cross-compiling=yes install_root=${D} \
	includedir='${includedir}' prefix='${prefix}' \
	install-bootstrap-headers=yes install-headers

	oe_runmake csu/subdir_lib
	# Two headers -- stubs.h and features.h -- aren't installed by install-headers,
	# so do them by hand.  We can tolerate an empty stubs.h for the moment.
	# See e.g. http://gcc.gnu.org/ml/gcc/2002-01/msg00900.html
	mkdir -p ${D}${includedir}/gnu/
	touch ${D}${includedir}/gnu/stubs.h
	cp ${S}/include/features.h ${D}${includedir}/features.h

	if [ -e ${B}/bits/stdio_lim.h ]; then
		cp ${B}/bits/stdio_lim.h  ${D}${includedir}/bits/
	fi
	mkdir -p ${D}${libdir}/
	install -m 644 csu/crt[1in].o ${D}${libdir}
	${CC} -nostdlib -nostartfiles -shared -x c /dev/null \
		-o ${D}${libdir}/libc.so

	# add links to linux-libc-headers: gcc-{cross,crossdk}-intermediate need this.
	for t in linux asm asm-generic; do
		ln -s ${STAGING_DIR_TARGET}${includedir}/$t ${D}${includedir}/
	done
}

do_siteconfig () {
	:
}

do_populate_sysroot[sstate-outputdirs] = "${STAGING_DIR_TCBOOTSTRAP}"
