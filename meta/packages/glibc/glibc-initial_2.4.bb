SECTION = "libs"
include glibc_${PV}.bb

DEPENDS = "linux-libc-headers"
PROVIDES = "virtual/${TARGET_PREFIX}libc-initial"
FILESPATH = "${@base_set_filespath([ '${FILE_DIRNAME}/glibc-cvs', '${FILE_DIRNAME}/glibc', '${FILE_DIRNAME}/files', '${FILE_DIRNAME}' ], d)}"

PACKAGES = ""

do_configure () {
	sed -ie 's,{ (exit 1); exit 1; }; },{ (exit 0); }; },g' ${S}/configure
	chmod +x ${S}/configure
	CC="${BUILD_CC}" CPP="${BUILD_CPP}" LD="${BUILD_LD}" ${S}/configure --host=${TARGET_SYS} --build=${BUILD_SYS} \
		--without-cvs --disable-sanity-checks \
		--with-headers=${CROSS_DIR}/${TARGET_SYS}/include \
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

do_stage () {
	oe_runmake cross-compiling=yes install_root=${CROSS_DIR}/${TARGET_SYS} prefix="" install-headers

	# Two headers -- stubs.h and features.h -- aren't installed by install-headers,
	# so do them by hand.  We can tolerate an empty stubs.h for the moment.
	# See e.g. http://gcc.gnu.org/ml/gcc/2002-01/msg00900.html
	mkdir -p ${CROSS_DIR}/${TARGET_SYS}/include/gnu
	touch ${CROSS_DIR}/${TARGET_SYS}/include/gnu/stubs.h
	cp ${S}/include/features.h ${CROSS_DIR}/${TARGET_SYS}/include/features.h
}

do_install () {
	:
}
