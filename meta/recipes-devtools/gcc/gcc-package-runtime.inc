PACKAGES = "\
  ${PN}-dbg \
  libstdc++ \
  libstdc++-precompile-dev \
  libstdc++-dev \
  libstdc++-staticdev \
  libg2c \
  libg2c-dev \
  libssp \
  libssp-dev \
  libssp-staticdev \
  libgfortran \
  libgfortran-dev \
  libmudflap \
  libmudflap-dev \
  libgomp \
  libgomp-dev \
  libgomp-staticdev \
"
# The base package doesn't exist, so we clear the recommends.
RRECOMMENDS_${PN}-dbg = ""

# include python debugging scripts
FILES_${PN}-dbg += "\
  ${libdir}/libstdc++.so.*-gdb.py \
  ${datadir}/gcc-${BINV}/python/libstdcxx"

FILES_libg2c = "${target_libdir}/libg2c.so.*"
FILES_libg2c-dev = "\
  ${libdir}/libg2c.so \
  ${libdir}/libg2c.a \
  ${libdir}/libfrtbegin.a"

FILES_libstdc++ = "${libdir}/libstdc++.so.*"
FILES_libstdc++-dev = "\
  ${includedir}/c++/ \
  ${libdir}/libstdc++.so \
  ${libdir}/libstdc++.la \
  ${libdir}/libsupc++.la"
FILES_libstdc++-staticdev = "\
  ${libdir}/libstdc++.a \
  ${libdir}/libsupc++.a"

FILES_libstdc++-precompile-dev = "${includedir}/c++/${TARGET_SYS}/bits/*.gch"

FILES_libssp = "${libdir}/libssp.so.*"
FILES_libssp-dev = " \
  ${libdir}/libssp*.so \
  ${libdir}/libssp*_nonshared.a \
  ${libdir}/libssp*.la \
  ${libdir}/gcc/${TARGET_SYS}/${BINV}/include/ssp"
FILES_libssp-staticdev = " \
  ${libdir}/libssp*.a"

FILES_libgfortran = "${libdir}/libgfortran.so.*"
FILES_libgfortran-dev = " \
  ${libdir}/libgfortran.a \
  ${libdir}/libgfortran.so \
  ${libdir}/libgfortranbegin.a"

FILES_libmudflap = "${libdir}/libmudflap*.so.*"
FILES_libmudflap-dev = "\
  ${libdir}/libmudflap*.so \
  ${libdir}/libmudflap*.a \
  ${libdir}/libmudflap*.la"

FILES_libgomp = "${libdir}/libgomp*${SOLIBS}"
FILES_libgomp-dev = "\
  ${libdir}/libgomp*${SOLIBSDEV} \
  ${libdir}/libgomp*.la \
  ${libdir}/libgomp.spec \
  ${libdir}/gcc/${TARGET_SYS}/${BINV}/include/omp.h \
  "
FILES_libgomp-staticdev = "\
  ${libdir}/libgomp*.a \
  "
