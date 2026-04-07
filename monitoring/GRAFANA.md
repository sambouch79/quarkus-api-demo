Dashboard Grafana - Métriques API quarkus-api-demo
📊 Vue d'ensemble
Ce dashboard permet de monitorer les endpoints de l'API quarkus-api-demo via Prometheus et Micrometer.

🔧 Configuration
1. Métriques générées
L'interceptor @MeasuredEndpoint génère automatiquement 3 métriques Prometheus :

api_endpoint_calls_total : Compteur d'appels réussis

Tags : endpoint, operation
api_endpoint_errors_total : Compteur d'erreurs

Tags : endpoint, operation
api_endpoint_latency_seconds : Timer de latence (avec histogramme)

Tags : endpoint, operation
Percentiles : P50, P95, P99
2. Utilisation de l'annotation
@GET
@Path("/current")
@MeasuredEndpoint(name = "tarifPrestation.current", endpoint = "getCurrentTarifPrestation")
public Response getCurrentTarifPrestation() {
    // votre code
}
Paramètres :

name : Nom de l'opération (utilisé pour le tag operation)
endpoint : Nom de l'endpoint (utilisé pour le tag endpoint et le filtrage Grafana)
3. Import du dashboard dans Grafana
Option A : Via l'interface Grafana
Ouvrez Grafana (http://localhost:3000)
Allez dans Dashboards > Import
Cliquez sur Upload JSON file
Sélectionnez monitoring/grafana-dashboard.json
Sélectionnez votre datasource Prometheus
Cliquez sur Import
Option B : Via l'API Grafana
$dashboard = Get-Content monitoring/grafana-dashboard.json | ConvertFrom-Json
$body = @{
    dashboard = $dashboard
    overwrite = $true
} | ConvertTo-Json -Depth 100

Invoke-RestMethod -Uri "http://localhost:3000/api/dashboards/db" `
    -Method POST `
    -Headers @{"Authorization"="Bearer YOUR_API_KEY"; "Content-Type"="application/json"} `
    -Body $body
4. Configuration Prometheus
Vérifiez que votre prometheus.yml scrape bien l'endpoint Quarkus :

global:
  scrape_interval: 5s

scrape_configs:
  - job_name: 'quarkus-api-demo'
    metrics_path: '/q/metrics'
    static_configs:
      - targets: ['host.docker.internal:8080']
📈 Panneaux du dashboard
Statistiques globales (ligne 1)
Total Calls : Nombre total d'appels réussis
Total Errors : Nombre total d'erreurs
Latency P95 (Global) : 95e percentile de latence global
Success Rate : Taux de succès global
Graphiques temporels
Latency Percentiles (par endpoint) : P50, P95, P99 par endpoint
Request Rate (par endpoint) : Nombre de requêtes/sec par endpoint
Error Rate (par endpoint) : Nombre d'erreurs/sec par endpoint
Success Rate (par endpoint) : Taux de succès par endpoint
Tableau récapitulatif
Endpoints Overview : Vue tabulaire avec toutes les métriques par endpoint
🔍 Filtres
Le dashboard dispose d'une variable Endpoint en haut qui permet de filtrer les graphiques par endpoint(s).

🧪 Test des métriques
1. Démarrez l'application
./mvnw quarkus:dev
2. Vérifiez que les métriques sont exposées
Invoke-WebRequest -Uri "http://localhost:8080/q/metrics" | Select-Object -ExpandProperty Content | Select-String "api_endpoint"
Vous devriez voir :


# Taux d'erreur
sum(rate(api_endpoint_errors_total[1m])) by (endpoint)
🐛 Dépannage
Les métriques n'apparaissent pas dans Prometheus
Vérifiez que Quarkus expose les métriques : http://localhost:8080/q/metrics
Vérifiez les targets Prometheus : http://localhost:9090/targets
Vérifiez les logs Prometheus pour les erreurs de scraping
Le dashboard Grafana est vide
Vérifiez que la datasource Prometheus est bien configurée dans Grafana
Vérifiez que vous avez généré du trafic vers les endpoints
Dans Grafana, allez dans Explore et testez manuellement :
api_endpoint_calls_total
Vérifiez la variable $endpoint en haut du dashboard
Les percentiles ne s'affichent pas
Les percentiles nécessitent plusieurs buckets pour être calculés. Générez plus de trafic avec des latences variées.

🎯 Bonnes pratiques
Nommage cohérent : Utilisez des noms d'endpoints descriptifs et cohérents
Tags pertinents : Le tag endpoint permet le filtrage, le tag operation donne le détail
Évitez la cardinalité élevée : Ne mettez pas d'IDs ou de valeurs dynamiques dans les tags
Testez en dev : Vérifiez les métriques avant de déployer en production
📚 Références
Micrometer Documentation
Quarkus Micrometer Guide
Prometheus Query Functions
Grafana Dashboard Best Practices