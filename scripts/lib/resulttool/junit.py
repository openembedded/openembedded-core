# resulttool - report test results in JUnit XML format
#
# Copyright (c) 2024, Siemens AG.
#
# SPDX-License-Identifier: GPL-2.0-only
#

import os
import xml.etree.ElementTree as ET
import resulttool.resultutils as resultutils


DEFAULT_JUNIT_FILE = "junit.xml"


class PtestSummary:
    """Collected infromation of one ptest suite

    Collect the information of many ptests of a ptest suite such as ptestresults.APtest.test_foo1,
    ptestresults.APtest.test_foo2, ... as one testcase APtest. This can be merged into information
    from ptestresult.sections.
    """
    def __init__(self):
        self.tests = 0
        self.ERROR = []
        self.FAILED = []
        self.SKIPPED = []

    def add_status(self, ptest_name, status):
        self.tests += 1
        if status == "FAILED":
            self.FAILED.append(ptest_name)
        elif status == "ERROR":
            self.ERROR.append(ptest_name)
        elif status == "SKIPPED":
            self.SKIPPED.append(ptest_name)

    @property
    def status(self):
        """Normalize the status of many ptests to one status of the ptest suite"""
        if len(self.ERROR) > 0:
            return "ERROR"
        if len(self.FAILED) > 0:
            return "FAILED"
        if len(self.SKIPPED) == self.tests:
            return "SKIPPED"
        return "SUCCESS"

    @property
    def log_summary(self):
        """Return a summary of the ptest suite"""
        summary_str = "ERROR:" + os.linesep
        summary_str += os.linesep.join([s + "- " for s in self.ERROR]) + os.linesep
        summary_str = "FAILED:" + os.linesep
        summary_str += os.linesep.join([s + "- " for s in self.FAILED]) + os.linesep
        summary_str = "SKIPPED:" + os.linesep
        summary_str += os.linesep.join([s + "- " for s in self.SKIPPED]) + os.linesep
        return summary_str


def create_testcase(testsuite, testcase_dict, status, status_message, status_text=None, system_out=None):
    """Create a junit testcase node"""
    testcase_node = ET.SubElement(testsuite, "testcase", testcase_dict)

    se = None
    if status == "SKIPPED":
        se = ET.SubElement(testcase_node, "skipped", message=status_message)
    elif status == "FAILED":
        se = ET.SubElement(testcase_node, "failure", message=status_message)
    elif status == "ERROR":
        se = ET.SubElement(testcase_node, "error", message=status_message)
    if se and status_text:
        se.text = status_text

    if system_out:
        ET.SubElement(testcase_node, "system-out").text = system_out


def junit_tree(testresults, test_log_dir=None):
    """Create a JUnit XML tree from testresults

    Generates a tuple of the XML tree and a dictionary of ptest log files.
    The dictionary contains the path where the log file is located as key and the log content as value.
    The log file path is test_log_dir/ptest_name.log.
    """
    test_logfiles = {}
    testsuites_node = ET.Element("testsuites")
    total_errors = total_failures = total_skipped = total_tests = total_time = 0

    for _, run_name, _, results in resultutils.test_run_results(testresults):
        test_run_testsuite = ET.SubElement(testsuites_node, "testsuite", name=run_name)

        # Handle all image tests but skip all ptests related sections
        imagetest_testsuite = ET.SubElement(test_run_testsuite, "testsuite", name="Image Tests")
        image_errors = image_failures = image_skipped = image_tests = image_total_time = 0

        ptest_summarys = {}

        for result_id, result in results.items():
            if result_id.startswith("ptestresult.sections") or result_id.startswith("ptestresult.rawlogs"):
                continue

            if result_id.startswith("ptestresult."):
                ptest_name = result_id.split(".", 3)[1]
                if ptest_name not in ptest_summarys:
                    ptest_summarys[ptest_name] = PtestSummary()
                ptest_summarys[ptest_name].add_status(ptest_name, result["status"])
            else:
                image_total_time += int(result["duration"])
                image_tests += 1
                status = result["status"]
                if status == "FAILED":
                    image_failures += 1
                elif status == "ERROR":
                    image_errors += 1
                elif status == "SKIPPED":
                    image_skipped += 1

                testcase_dict = {
                    "name": result_id,
                    "classname": "testimage",
                    "time": str(result["duration"]),
                }
                create_testcase(
                    imagetest_testsuite,
                    testcase_dict,
                    status,
                    result.get("log", None))

        imagetest_testsuite.set("errors", str(image_errors))
        imagetest_testsuite.set("failures", str(image_failures))
        imagetest_testsuite.set("skipped", str(image_skipped))
        imagetest_testsuite.set("tests", str(image_tests))
        imagetest_testsuite.set("time", str(image_total_time))

        # Handle all ptest related sections
        ptest_errors = ptest_failures = ptest_skipped = ptest_tests = ptest_total_time = 0
        if "ptestresult.sections" in results:
            ptest_testsuite = ET.SubElement(test_run_testsuite, "testsuite", name="Package Tests")

            for ptest_name, result in results["ptestresult.sections"].items():
                testcase_dict = {
                    "name": ptest_name,
                    "classname": "ptest",
                    "time": str(result["duration"]),
                }

                log = result.get("log")
                system_out = None
                if log:
                    if test_log_dir is not None:
                        test_log_file = os.path.join(test_log_dir, ptest_name + ".log")
                        system_out = f"[[ATTACHMENT|{test_log_file}]]"
                        test_logfiles[test_log_file] = log
                    else:
                        system_out = log

                create_testcase(ptest_testsuite,
                                testcase_dict,
                                ptest_summarys[ptest_name].status,
                                ptest_summarys[ptest_name].log_summary,
                                system_out=system_out)

                ptest_total_time += int(result["duration"])
                ptest_tests += 1
                status = ptest_summarys[ptest_name].status
                if status == "FAILED":
                    ptest_failures += 1
                elif status == "ERROR":
                    ptest_errors += 1
                elif status == "SKIPPED":
                    ptest_skipped += 1

            ptest_testsuite.set("errors", str(ptest_errors))
            ptest_testsuite.set("failures", str(ptest_failures))
            ptest_testsuite.set("skipped", str(ptest_skipped))
            ptest_testsuite.set("tests", str(ptest_tests))
            ptest_testsuite.set("time", str(ptest_total_time))

        total_errors += image_errors + ptest_errors
        total_failures += image_failures + ptest_failures
        total_skipped += image_skipped + ptest_skipped
        total_tests += image_tests + ptest_tests
        total_time += image_total_time + ptest_total_time

    testsuites_node.set("errors", str(total_errors))
    testsuites_node.set("failures", str(total_failures))
    testsuites_node.set("skipped", str(total_skipped))
    testsuites_node.set("tests", str(total_tests))
    testsuites_node.set("time", str(total_time))
    tree = ET.ElementTree(testsuites_node)
    return tree, test_logfiles


def junit(args, logger):
    if args.junit_xml_path is not None:
        junit_xml_path = args.junit_xml_path
    else:
        junit_xml_path = os.path.join(os.path.dirname(args.json_file), DEFAULT_JUNIT_FILE)
    logger.debug("Generating JUnit XML report from %s" % args.json_file)
    testresults = resultutils.load_resultsdata(args.json_file, configmap=resultutils.store_map)

    # dump ptest logs to a file in a subdirectory where the junit.xml is located
    test_log_dir = None
    if args.attach_log_files:
        test_log_dir = "test-logs"
        junit_dir_abs = os.path.dirname(os.path.abspath(junit_xml_path))
        test_log_dir_abs = os.path.join(junit_dir_abs, test_log_dir)
        if not os.path.exists(test_log_dir_abs):
            os.makedirs(test_log_dir_abs)

    tree, test_logfiles = junit_tree(testresults, test_log_dir)

    for test_logfile, log in test_logfiles.items():
        with open(os.path.join(junit_dir_abs, test_logfile), "w") as f:
            f.write(log)

    tree.write(junit_xml_path, encoding="UTF-8", xml_declaration=True)
    logger.info("Saved JUnit XML report as %s" % junit_xml_path)


def register_commands(subparsers):
    """Register subcommands from this plugin"""
    parser_build = subparsers.add_parser("junit",
        help="create test report in JUnit XML format",
        description="generate unit test report in JUnit XML format based on the latest test results in the testresults.json.",
        group="analysis"
    )
    parser_build.set_defaults(func=junit)

    # If BUILDDIR is set, use the default path for the testresults.json and junit.xml
    # Otherwise require the user to provide the path to the testresults.json
    help_json_file = "json file should point to the testresults.json"
    help_junit_xml_path = "junit xml path allows setting the path of the generated test report."
    try:
        builddir = os.environ["BUILDDIR"]
    except KeyError:
        builddir = None
    if builddir:
        log_path = os.path.join(builddir, "tmp", "log", "oeqa")
        parser_build.add_argument("json_file", nargs="?",
            default=os.path.join(log_path, "testresults.json"),
            help=help_json_file + " (default: %(default)s)")
        parser_build.add_argument("-j", "--junit_xml_path",
            default=os.path.join(log_path, DEFAULT_JUNIT_FILE),
            help=help_junit_xml_path + " (default: %(default)s)")
    else:
        parser_build.add_argument("json_file", help=help_json_file)
        parser_build.add_argument("-j", "--junit_xml_path", nargs="?",
            help=help_junit_xml_path + " (default: junit.xml in the same folder as the testresults.json)")

    parser_build.add_argument("-a", "--attach-log-files", action="store_true", default=False,
        help="Write the log files to subfolder in the same folder as the junit.xml and reference them in the junit.xml")
