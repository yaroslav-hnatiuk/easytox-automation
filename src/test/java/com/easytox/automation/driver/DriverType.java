package com.easytox.automation.driver;

import org.apache.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.openqa.selenium.remote.CapabilityType.PROXY;

public enum DriverType implements DriverSetup {

    FIREFOX {
        public DesiredCapabilities getDesiredCapabilities(Proxy proxySettings) {
            String platform = System.getProperty("os.name");
            DesiredCapabilities capabilities = DesiredCapabilities.firefox();
            if (platform.toLowerCase().startsWith("mac")) {
                capabilities.setPlatform(Platform.MAC);
                System.setProperty("webdriver.chrome.driver", "./drivers/geckodriver");
            } else if ("linux".equalsIgnoreCase(platform)) {
                capabilities.setPlatform(Platform.LINUX);
                System.setProperty("webdriver.chrome.driver", "./drivers/geckodriver_linux");
            } else if ("unix".equalsIgnoreCase(platform)) {
                capabilities.setPlatform(Platform.UNIX);
                System.setProperty("webdriver.chrome.driver", "./drivers/geckodriver");
            } else {
                capabilities.setPlatform(Platform.ANY);
                System.setProperty("webdriver.chrome.driver", "C:/selenium-drivers/geckodriver.exe");
            }
            return addProxySettings(capabilities, proxySettings);
        }

        public WebDriver getWebDriverObject(DesiredCapabilities capabilities) {
            return new FirefoxDriver(capabilities);
        }

    },
    CHROME {
        public DesiredCapabilities getDesiredCapabilities(Proxy proxySettings) {
            String platform = System.getProperty("os.name");

            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
            capabilities.setCapability("disable-popup-blocking", false);
            capabilities.setCapability("chrome.switches", Arrays.asList("--no-default-browser-check"));
            HashMap<String, String> chromePreferences = new HashMap<String, String>();
            chromePreferences.put("profile.password_manager_enabled", "false");
            capabilities.setCapability("chrome.prefs", chromePreferences);

            if (platform.toLowerCase().startsWith("mac")) {
                capabilities.setPlatform(Platform.MAC);
                System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver");
            } else if ("linux".equalsIgnoreCase(platform)) {
                capabilities.setPlatform(Platform.LINUX);
                System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver_linux");
            } else if ("unix".equalsIgnoreCase(platform)) {
                capabilities.setPlatform(Platform.UNIX);
                System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver");
            } else {
                capabilities.setPlatform(Platform.ANY);
                System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
            }
            return addProxySettings(capabilities, proxySettings);
        }

        public WebDriver getWebDriverObject(DesiredCapabilities capabilities) {
            return new ChromeDriver(capabilities);
        }
    },
    IE {
        public DesiredCapabilities getDesiredCapabilities(Proxy proxySettings) {
            DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
            capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
            capabilities.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, true);
            capabilities.setCapability("requireWindowFocus", true);
            return addProxySettings(capabilities, proxySettings);
        }

        public WebDriver getWebDriverObject(DesiredCapabilities capabilities) {
            return new InternetExplorerDriver(capabilities);
        }
    },
    SAFARI {
        public DesiredCapabilities getDesiredCapabilities(Proxy proxySettings) {
            DesiredCapabilities capabilities = DesiredCapabilities.safari();
            capabilities.setCapability("safari.cleanSession", true);
            return addProxySettings(capabilities, proxySettings);
        }

        public WebDriver getWebDriverObject(DesiredCapabilities capabilities) {
            return new SafariDriver(capabilities);
        }
    },
    OPERA {
        public DesiredCapabilities getDesiredCapabilities(Proxy proxySettings) {
            DesiredCapabilities capabilities = DesiredCapabilities.operaBlink();
            return addProxySettings(capabilities, proxySettings);
        }

        public WebDriver getWebDriverObject(DesiredCapabilities capabilities) {
            return new OperaDriver(capabilities);
        }
    };
//    PHANTOMJS {
//        public DesiredCapabilities getDesiredCapabilities(Proxy proxySettings) {
//            DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();
//            final List<String> cliArguments = new ArrayList<String>();
//            cliArguments.add("--web-security=false");
//            cliArguments.add("--ssl-protocol=any");
//            cliArguments.add("--ignore-ssl-errors=true");
//            capabilities.setCapability("phantomjs.cli.args", applyPhantomJSProxySettings(cliArguments, proxySettings));
//            capabilities.setCapability("takesScreenshot", true);
//
//            return capabilities;
//        }
//
//        public WebDriver getWebDriverObject(DesiredCapabilities capabilities) {
//            return new PhantomJSDriver(capabilities);
//        }
//    };

    private static final Logger LOGGER = Logger.getLogger(DriverType.class);

    protected DesiredCapabilities addProxySettings(DesiredCapabilities capabilities, Proxy proxySettings) {
        if (null != proxySettings) {
            capabilities.setCapability(PROXY, proxySettings);
        }

        return capabilities;
    }

    protected List<String> applyPhantomJSProxySettings(List<String> cliArguments, Proxy proxySettings) {
        if (null == proxySettings) {
            cliArguments.add("--proxy-type=none");
        } else {
            cliArguments.add("--proxy-type=http");
            cliArguments.add("--proxy=" + proxySettings.getHttpProxy());
        }
        return cliArguments;
    }
}
