# Copyright (C) 2017 Intel Corporation
# Released under the MIT license (see COPYING.MIT)

import threading
import multiprocessing

from unittest.suite import TestSuite

from oeqa.core.loader import OETestLoader
from oeqa.core.runner import OEStreamLogger

class OETestLoaderThreaded(OETestLoader):
    def __init__(self, tc, module_paths, modules, tests, modules_required,
            filters, process_num=0, *args, **kwargs):
        super(OETestLoaderThreaded, self).__init__(tc, module_paths, modules,
                tests, modules_required, filters, *args, **kwargs)

        self.process_num = process_num

    def discover(self):
        suite = super(OETestLoaderThreaded, self).discover()

        if self.process_num <= 0:
            self.process_num = min(multiprocessing.cpu_count(),
                    len(suite._tests))

        suites = []
        for _ in range(self.process_num):
            suites.append(self.suiteClass())

        def _search_for_module_idx(suites, case):
            """
                Cases in the same module needs to be run
                in the same thread because PyUnit keeps track
                of setUp{Module, Class,} and tearDown{Module, Class,}.
            """

            for idx in range(self.process_num):
                suite = suites[idx]
                for c in suite._tests:
                    if case.__module__ == c.__module__:
                        return idx

            return -1

        def _search_for_depend_idx(suites, depends):
            """
                Dependency cases needs to be run in the same
                thread, because OEQA framework look at the state
                of dependant test to figure out if skip or not.
            """

            for idx in range(self.process_num):
                suite = suites[idx]

                for case in suite._tests:
                    if case.id() in depends:
                        return idx
            return -1

        def _get_best_idx(suites):
            sizes = [len(suite._tests) for suite in suites]
            return sizes.index(min(sizes))

        def _fill_suites(suite):
            idx = -1
            for case in suite:
                if isinstance(case, TestSuite):
                    _fill_suites(case)
                else:
                    idx = _search_for_module_idx(suites, case)

                    depends = {}
                    if 'depends' in self.tc._registry:
                        depends = self.tc._registry['depends']

                    if idx == -1 and case.id() in depends:
                        case_depends = depends[case.id()] 
                        idx = _search_for_depend_idx(suites, case_depends)

                    if idx == -1:
                        idx = _get_best_idx(suites)

                    suites[idx].addTest(case)
        _fill_suites(suite)

        suites_tmp = suites
        suites = []
        for suite in suites_tmp:
            if len(suite._tests) > 0:
                suites.append(suite)

        return suites

class OEStreamLoggerThreaded(OEStreamLogger):
    _lock = threading.Lock()
    buffers = {}

    def write(self, msg):
        tid = threading.get_ident()

        if not tid in self.buffers:
            self.buffers[tid] = ""

        if msg:
            self.buffers[tid] += msg

    def finish(self):
        tid = threading.get_ident()
        
        self._lock.acquire()
        self.logger.info('THREAD: %d' % tid)
        self.logger.info('-' * 70)
        for line in self.buffers[tid].split('\n'):
            self.logger.info(line)
        self._lock.release()
