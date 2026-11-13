## Copyright (C) 2016 Intel Corporation
#
# SPDX-License-Identifier: MIT
#

import os
import sys
import json
import time
import logging
import collections
import unittest

from oeqa.core.loader import OETestLoader
from oeqa.core.runner import OETestRunner
from oeqa.core.exception import OEQAMissingManifest, OEQATestNotFound

class OETestContext:
    loaderClass = OETestLoader
    runnerClass = OETestRunner

    files_dir = os.path.abspath(os.path.join(os.path.dirname(
        os.path.abspath(__file__)), "../files"))

    def __init__(self, td=None, logger=None):
        if not isinstance(td, dict):
            raise TypeError("td isn't dictionary type")

        self.td = td
        self.logger = logger
        self._registry = {}
        self._registry['cases'] = collections.OrderedDict()

        self.results = unittest.TestResult()
        unittest.registerResult(self.results)

    def _read_modules_from_manifest(self, manifest):
        if not os.path.exists(manifest):
            raise OEQAMissingManifest("Manifest does not exist on %s" % manifest)

        modules = []
        for line in open(manifest).readlines():
            line = line.strip()
            if line and not line.startswith("#"):
                modules.append(line)

        return modules

    def skipTests(self, skips):
        # Use unittest's skip support so that skipped tests, and the class
        # setup of skipped classes, don't run
        for test in self.suites:
            class_id = test.id().rsplit('.', 1)[0]
            for skip in skips:
                msg = 'Skip by the command line argument "%s"' % skip
                if (class_id + '.').startswith(skip + '.'):
                    unittest.skip(msg)(test.__class__)
                elif (test.id() + '.').startswith(skip + '.'):
                    method = getattr(test, test._testMethodName)
                    setattr(test, test._testMethodName, unittest.skip(msg)(method))

    def loadTests(self, module_paths, modules=[], tests=[],
            modules_manifest="", modules_required=[], **kwargs):
        if modules_manifest:
            modules = self._read_modules_from_manifest(modules_manifest)

        self.loader = self.loaderClass(self, module_paths, modules, tests,
                modules_required, **kwargs)
        self.suites = self.loader.discover()

    def prepareSuite(self, suites, processes):
        return suites

    def runTests(self, processes=None, skips=[]):
        self.runner = self.runnerClass(self, descriptions=False, verbosity=2)

        # Dynamically skip those tests specified though arguments
        self.skipTests(skips)

        self._run_start_time = time.time()
        self._run_end_time = self._run_start_time
        if not processes:
            self.runner.buffer = True
        result = self.runner.run(self.prepareSuite(self.suites, processes))
        self._run_end_time = time.time()

        return result

    def listTests(self, display_type):
        self.runner = self.runnerClass(self, verbosity=2)
        return self.runner.list_tests(self.suites, display_type)

class OETestContextExecutor:
    _context_class = OETestContext
    _script_executor = 'oe-test'

    name = 'core'
    help = 'core test component example'
    description = 'executes core test suite example'
    datetime = time.strftime("%Y%m%d%H%M%S")

    default_cases = [os.path.join(os.path.abspath(os.path.dirname(__file__)),
            'cases/example')]
    default_test_data = os.path.join(default_cases[0], 'data.json')
    default_tests = None
    default_json_result_dir = None

    def register_commands(self, logger, subparsers):
        self.parser = subparsers.add_parser(self.name, help=self.help,
                description=self.description, group='components')

        self.default_output_log = '%s-results-%s.log' % (self.name, self.datetime)
        self.parser.add_argument('--output-log', action='store',
                default=self.default_output_log,
                help="results output log, default: %s" % self.default_output_log)

        self.parser.add_argument('--json-result-dir', action='store',
                default=self.default_json_result_dir,
                help="json result output dir, default: %s" % self.default_json_result_dir)

        group = self.parser.add_mutually_exclusive_group()
        group.add_argument('--run-tests', action='store', nargs='+',
                default=self.default_tests,
                help="tests to run in <module>[.<class>[.<name>]]")
        group.add_argument('--list-tests', action='store',
                choices=('module', 'class', 'name'),
                help="lists available tests")

        if self.default_test_data:
            self.parser.add_argument('--test-data-file', action='store',
                    default=self.default_test_data,
                    help="data file to load, default: %s" % self.default_test_data)
        else:
            self.parser.add_argument('--test-data-file', action='store',
                    help="data file to load")

        if self.default_cases:
            self.parser.add_argument('CASES_PATHS', action='store',
                    default=self.default_cases, nargs='*',
                    help="paths to directories with test cases, default: %s"\
                            % self.default_cases)
        else:
            self.parser.add_argument('CASES_PATHS', action='store',
                    nargs='+', help="paths to directories with test cases")

        self.parser.set_defaults(func=self.run)

    def _setup_logger(self, logger, args):
        formatter = logging.Formatter('%(asctime)s - ' + self.name + \
                ' - %(levelname)s - %(message)s')
        sh = logger.handlers[0]
        sh.setFormatter(formatter)
        fh = logging.FileHandler(args.output_log)
        fh.setFormatter(formatter)
        logger.addHandler(fh)
        if getattr(args, 'verbose', False):
            logger.setLevel('DEBUG')

        return logger

    def _process_args(self, logger, args):
        self.logger = self._setup_logger(logger, args)
        if args.test_data_file:
            with open(args.test_data_file, "r") as f:
                self.td = json.load(f)
        else:
            self.td = {}

        # Any further arguments for the test context, for subclasses to add
        self.context_args = {}

        # Run image specific TEST_SUITE like testimage.bbclass by default
        test_suites = self.td.get("TEST_SUITES")
        if test_suites:
            test_suites = test_suites.split()

        self.modules_required = []
        if args.run_tests:
            self.modules = args.run_tests
            self.modules_required = args.run_tests
        elif test_suites:
            self.modules = test_suites
        else:
            self.modules = []
        self.tags_filter = None

        self.processes = None
        self.skips = []

        self.module_paths = args.CASES_PATHS

    def _get_json_result_dir(self, args):
        return args.json_result_dir

    def _get_configuration(self):
        td = self.td
        configuration = {'TEST_TYPE': self.name,
                        'MACHINE': td.get("MACHINE"),
                        'DISTRO': td.get("DISTRO"),
                        'IMAGE_BASENAME': td.get("IMAGE_BASENAME"),
                        'DATETIME': td.get("DATETIME")}
        return configuration

    def _get_result_id(self, configuration):
        return '%s_%s_%s_%s' % (configuration['TEST_TYPE'], configuration['IMAGE_BASENAME'],
                                configuration['MACHINE'], self.datetime)

    def _pre_run(self):
        pass

    def run(self, logger, args):
        self._process_args(logger, args)

        self.tc = self._context_class(td=self.td, logger=self.logger, **self.context_args)
        try:
            self.tc.loadTests(self.module_paths, modules=self.modules,
                    modules_required=self.modules_required, tags_filter=self.tags_filter)
        except OEQATestNotFound as ex:
            logger.error(ex)
            sys.exit(1)

        if args.list_tests:
            rc = self.tc.listTests(args.list_tests)
        else:
            self._pre_run()
            rc = self.tc.runTests(processes=self.processes, skips=self.skips)

            json_result_dir = self._get_json_result_dir(args)
            if json_result_dir:
                configuration = self._get_configuration()
                rc.logDetails(json_result_dir,
                              configuration,
                              self._get_result_id(configuration))
            else:
                rc.logDetails()

            rc.logSummary(self.name)

        output_link = os.path.join(os.path.dirname(args.output_log),
                "%s-results.log" % self.name)
        if os.path.exists(output_link):
            os.remove(output_link)
        os.symlink(args.output_log, output_link)

        return rc

_executor_class = OETestContextExecutor
